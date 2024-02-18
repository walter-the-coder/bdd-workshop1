package features.stepdefs;

import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.repository.TransactionRepository;

import features.stepdefs.util.ReceptionDtoUtil;
import io.cucumber.java.en.And;

public class TransactionStepDef {
    private final TransactionRepository transactionRepository;

    public TransactionStepDef(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @And("the following record should exist in the database as unprocessed")
    public void the_following_record_should_exist_in_the_database(Map<String, String> dataTable) {
        ReceptionDto data = ReceptionDtoUtil.getReceptionDto(dataTable);
        List<ReceptionDto> unprocessed = transactionRepository.getUnprocessedData();
        Assert.assertTrue(unprocessed.contains(data));
    }
}
