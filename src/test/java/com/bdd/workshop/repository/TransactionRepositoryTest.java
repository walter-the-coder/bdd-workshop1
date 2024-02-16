package com.bdd.workshop.repository;

import static com.bdd.workshop.ApplicationConfig.DEFAULT_OBJECT_MAPPER;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.VATCode;
import com.bdd.workshop.controller.dto.VATLine;
import com.bdd.workshop.controller.dto.VATLines;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.TaxCategory;
import com.bdd.workshop.type.TaxationPeriodType;
import com.bdd.workshop.type.TaxpayerIdentificationNumber;

public class TransactionRepositoryTest {
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        EmbeddedDatabase h2 =
            (new EmbeddedDatabaseBuilder())
                .setType(EmbeddedDatabaseType.H2)
                .build();

        Flyway flyway = Flyway
            .configure()
            .dataSource(h2)
            .cleanDisabled(false)
            .locations("classpath:/db/migration")
            .load();

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(h2);
        transactionRepository = new TransactionRepository(jdbcTemplate, DEFAULT_OBJECT_MAPPER);

        flyway.migrate();
    }

    @Test
    public void testStoreReceivedData() {
        List<VATLine> vatLines = new ArrayList<>();
        vatLines.add(
            VATLine.with()
                .withVATCode(VATCode.VAT_CODE_1)
                .withAmount(-1000.0)
                .build()
        );
        vatLines.add(
            VATLine.with()
                .withVATCode(VATCode.VAT_CODE_3)
                .withAmount(50000.0)
                .build()
        );

        ReceptionDto data = ReceptionDto.with()
            .withOrganisationNumber(new OrganisationNumber("123456789"))
            .withSubmitterId(new TaxpayerIdentificationNumber("01012012345"))
            .withCategory(TaxCategory.NORMAL)
            .withYear(2024)
            .withTaxationPeriodType(TaxationPeriodType.JAN_FEB)
            .withTimeOfSubmission(LocalDate.of(2024, 1, 1).atStartOfDay())
            .withVatLines(VATLines.with().withVATLines(vatLines).build())
            .build();

        Assertions.assertDoesNotThrow(() -> transactionRepository.storeReceivedData(data));

        List<ReceptionDto> unprocessed = transactionRepository.getUnprocessedData();
        Assertions.assertEquals(1, unprocessed.size());
        Assertions.assertEquals(data, unprocessed.get(0));
    }
}