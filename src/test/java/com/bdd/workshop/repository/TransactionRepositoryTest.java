package com.bdd.workshop.repository;

import static com.bdd.workshop.ApplicationConfig.DEFAULT_OBJECT_MAPPER;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.VATCode;
import com.bdd.workshop.controller.dto.VATLine;
import com.bdd.workshop.controller.dto.VATLines;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.PersonId;
import com.bdd.workshop.type.TaxCategory;
import com.bdd.workshop.type.TaxationPeriodType;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;

public class TransactionRepositoryTest {
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() throws IOException {
        EmbeddedPostgres pg = EmbeddedPostgres.start();
        Flyway.configure().dataSource(pg.getPostgresDatabase()).load().migrate();
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(pg.getPostgresDatabase());
        transactionRepository = new TransactionRepository(jdbcTemplate, DEFAULT_OBJECT_MAPPER);
    }

    @Test
    public void testStoreReceivedData() {
        ReceptionDto data = new ReceptionDto(
            new OrganisationNumber("123456789"),
            new PersonId("01012012345"),
            TaxCategory.NORMAL,
            2024,
            TaxationPeriodType.JAN_FEB,
            LocalDate.of(2024, 1, 1).atStartOfDay(),
            new VATLines(
                List.of(
                    new VATLine(
                        VATCode.VAT_CODE_1,
                        -1000.0
                    ),
                    new VATLine(
                        VATCode.VAT_CODE_3,
                        50000.0
                    )
                )
            )
        );

        Assertions.assertDoesNotThrow(() -> transactionRepository.storeReceivedData(data));

        List<ReceptionDto> unprocessed = transactionRepository.getUnprocessedData();
        Assertions.assertEquals(1, unprocessed.size());
        Assertions.assertEquals(data, unprocessed.get(0));
    }
}