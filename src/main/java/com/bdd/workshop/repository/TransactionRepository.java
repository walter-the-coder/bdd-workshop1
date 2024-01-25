package com.bdd.workshop.repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.exceptionHandling.CustomRuntimeException;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.PersonId;
import com.bdd.workshop.type.ReceptionStatus;
import com.bdd.workshop.type.TaxCategory;
import com.bdd.workshop.type.TaxationPeriodType;

@Repository
public class TransactionRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TransactionRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void storeReceivedData(ReceptionDto data) {
        Map<String, Object> params = new HashMap<>();
        params.put("organisationNumber", data.organisationNumber().toString());
        params.put("submitterId", data.submitterId().toString());
        params.put("category", data.category().toString());
        params.put("year", data.year());
        params.put("taxationPeriodType", data.taxationPeriodType().toString());
        params.put("timeOfSubmission", data.timeOfSubmission());
        params.put("status", ReceptionStatus.RECEIVED.name());

        jdbcTemplate.update("""
                INSERT INTO transactions (organisationNumber, submitterId, category, year, taxationPeriodType, timeOfSubmission, status)
                 VALUES (:organisationNumber, :submitterId, :category, :year, :taxationPeriodType, :timeOfSubmission, :status)
                """,
            params
        );
    }

    public List<ReceptionDto> getUnprocessedData() {
        return jdbcTemplate.query(
            "SELECT * FROM transactions WHERE status = :status",
            Map.of("status", ReceptionStatus.RECEIVED.name()),
            new ReceptionRowMapper()
        );
    }

    private static class ReceptionRowMapper implements RowMapper<ReceptionDto> {
        @Override
        public ReceptionDto mapRow(ResultSet rs, int rowNum) {
            try {
                return new ReceptionDto(
                    new OrganisationNumber(rs.getString("organisationNumber")),
                    new PersonId(rs.getString("submitterId")),
                    TaxCategory.valueOf(rs.getString("category")),
                    rs.getInt("year"),
                    TaxationPeriodType.valueOf(rs.getString("taxationPeriodType")),
                    rs.getObject("timeOfSubmission", LocalDateTime.class)
                );
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
}
