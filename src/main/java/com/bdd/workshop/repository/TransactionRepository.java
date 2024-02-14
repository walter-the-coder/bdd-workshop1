package com.bdd.workshop.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.postgresql.util.PGobject;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.VATLines;
import com.bdd.workshop.exceptionHandling.CustomRuntimeException;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.PersonId;
import com.bdd.workshop.type.ReceptionStatus;
import com.bdd.workshop.type.TaxCategory;
import com.bdd.workshop.type.TaxationPeriodType;
import com.bdd.workshop.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class TransactionRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public TransactionRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        ObjectMapper objectMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public void storeReceivedData(ReceptionDto data) {
        Map<String, Object> params = new HashMap<>();
        params.put("organisationNumber", data.getOrganisationNumber().toString());
        params.put("submitterId", data.getSubmitterId().toString());
        params.put("category", data.getCategory().toString());
        params.put("year", data.getYear());
        params.put("taxationPeriodType", data.getTaxationPeriodType().toString());
        params.put("timeOfSubmission", data.getTimeOfSubmission());
        params.put("status", ReceptionStatus.RECEIVED.name());
        params.put("vatLines", writeVATLines(data.getVatLines()));

        String sql =
            "INSERT INTO transactions ("
                + "organisationNumber, "
                + "submitterId, "
                + "category, "
                + "year, "
                + "taxationPeriodType, "
                + "timeOfSubmission, "
                + "status, "
                + "vatLines"
                + ")"
                + " VALUES ("
                + ":organisationNumber, "
                + ":submitterId, "
                + ":category, "
                + ":year, "
                + ":taxationPeriodType, "
                + ":timeOfSubmission, "
                + ":status, "
                + ":vatLines"
                + ")";

        jdbcTemplate.update(sql, params);
    }

    public List<ReceptionDto> getUnprocessedData() {
        Map<String, String> values = new HashMap<>();
        values.put("status", ReceptionStatus.RECEIVED.name());

        return jdbcTemplate.query(
            "SELECT * FROM transactions WHERE status = :status",
            values,
            this::mapRow
        );
    }

    public ReceptionDto mapRow(ResultSet rs, int rowNum) {
        try {
            return ReceptionDto.with()
                .withOrganisationNumber(new OrganisationNumber(rs.getString("organisationNumber")))
                .withSubmitterId(new PersonId(rs.getString("submitterId")))
                .withCategory(TaxCategory.valueOf(rs.getString("category")))
                .withYear(rs.getInt("year"))
                .withTaxationPeriodType(TaxationPeriodType.valueOf(rs.getString("taxationPeriodType")))
                .withTimeOfSubmission(rs.getObject("timeOfSubmission", LocalDateTime.class))
                .withVatLines(objectMapper.readValue(rs.getString("vatLines"), VATLines.class))
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

    private PGobject writeVATLines(VATLines vatLines) {
        PGobject jsonObject = new PGobject();
        String json = JsonUtil.writeJson(objectMapper, vatLines);
        jsonObject.setType("jsonb");
        try {
            jsonObject.setValue(json);
        } catch (SQLException e) {
            throw new CustomRuntimeException(
                "JSON_PGOBJECT_WRITING_ERROR",
                "Failed to write JSON string to PGobject",
                e,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return jsonObject;
    }
}
