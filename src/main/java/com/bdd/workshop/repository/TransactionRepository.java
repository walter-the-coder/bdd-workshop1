package com.bdd.workshop.repository;

import static com.bdd.workshop.util.JsonUtil.readJson;
import static com.bdd.workshop.util.JsonUtil.writeAsJsonString;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.VATLines;
import com.bdd.workshop.exceptionHandling.CustomRuntimeException;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.ReceptionStatus;
import com.bdd.workshop.type.TaxCategory;
import com.bdd.workshop.type.TaxationPeriodType;
import com.bdd.workshop.type.TaxpayerIdentificationNumber;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class TransactionRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public TransactionRepository(
        @Qualifier("mainDatasourceNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate,
        ObjectMapper objectMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public void storeReceivedData(ReceptionDto data) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("ORGANISATION_NUMBER", data.getOrganisationNumber().toString());
        paramSource.addValue("SUBMITTER_TIN", data.getSubmitterId().toString());
        paramSource.addValue("CATEGORY", data.getCategory().toString());
        paramSource.addValue("TAXATION_YEAR", data.getYear());
        paramSource.addValue("TAXATION_PERIOD_TYPE", data.getTaxationPeriodType().toString());
        paramSource.addValue("TIME_OF_SUBMISSION", data.getTimeOfSubmission());
        paramSource.addValue("STATUS", ReceptionStatus.RECEIVED.name());
        paramSource.addValue("VAT_LINES", writeAsJsonString(objectMapper, data.getVatLines()));

        String sql =
            "INSERT INTO TRANSACTIONS ("
                + "ORGANISATION_NUMBER, "
                + "SUBMITTER_TIN, "
                + "CATEGORY, "
                + "TAXATION_YEAR, "
                + "TAXATION_PERIOD_TYPE, "
                + "TIME_OF_SUBMISSION, "
                + "STATUS, "
                + "VAT_LINES"
                + ")"
                + " VALUES ("
                + ":ORGANISATION_NUMBER, "
                + ":SUBMITTER_TIN, "
                + ":CATEGORY, "
                + ":TAXATION_YEAR, "
                + ":TAXATION_PERIOD_TYPE, "
                + ":TIME_OF_SUBMISSION, "
                + ":STATUS, "
                + ":VAT_LINES"
                + ")";

        jdbcTemplate.update(sql, paramSource);
    }

    public List<ReceptionDto> getUnprocessedData() {
        Map<String, String> values = new HashMap<>();
        values.put("STATUS", ReceptionStatus.RECEIVED.name());

        return jdbcTemplate.query(
            "SELECT * FROM TRANSACTIONS WHERE STATUS = :STATUS",
            values,
            this::mapRow
        );
    }

    public ReceptionDto mapRow(ResultSet rs, int rowNum) {
        try {
            return ReceptionDto.with()
                .withOrganisationNumber(new OrganisationNumber(rs.getString("ORGANISATION_NUMBER")))
                .withSubmitterId(new TaxpayerIdentificationNumber(rs.getString("SUBMITTER_TIN")))
                .withCategory(TaxCategory.valueOf(rs.getString("CATEGORY")))
                .withYear(rs.getInt("TAXATION_YEAR"))
                .withTaxationPeriodType(TaxationPeriodType.valueOf(rs.getString("TAXATION_PERIOD_TYPE")))
                .withTimeOfSubmission(rs.getObject("TIME_OF_SUBMISSION", LocalDateTime.class))
                .withVatLines(readJson(objectMapper, rs.getString("VAT_LINES"), VATLines.class))
                .build();
        } catch (Exception e) {
            throw new CustomRuntimeException(
                "ROW_MAPPING_ERROR",
                "Error mapping row: " + e.getMessage(),
                e,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
