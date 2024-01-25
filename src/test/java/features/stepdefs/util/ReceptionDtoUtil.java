package features.stepdefs.util;

import java.time.LocalDateTime;
import java.util.Map;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.PersonId;
import com.bdd.workshop.type.TaxCategory;
import com.bdd.workshop.type.TaxationPeriodType;

public final class ReceptionDtoUtil {
    public static ReceptionDto getReceptionDto(Map<String, String> dataTable) {
        return new ReceptionDto(
            new OrganisationNumber(dataTable.get("organisationNumber")),
            new PersonId(dataTable.get("submitterId")),
            TaxCategory.valueOf(dataTable.get("taxCategory")),
            Integer.parseInt(dataTable.get("year")),
            TaxationPeriodType.valueOf(dataTable.get("taxationPeriod")),
            LocalDateTime.parse(dataTable.get("timeOfSubmission"))
        );
    }
}
