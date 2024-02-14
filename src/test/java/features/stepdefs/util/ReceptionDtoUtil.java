package features.stepdefs.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.VATCode;
import com.bdd.workshop.controller.dto.VATLine;
import com.bdd.workshop.controller.dto.VATLines;
import com.bdd.workshop.type.OrganisationNumber;
import com.bdd.workshop.type.PersonId;
import com.bdd.workshop.type.TaxCategory;
import com.bdd.workshop.type.TaxationPeriodType;

public final class ReceptionDtoUtil {
    public static ReceptionDto getReceptionDto(Map<String, String> dataTable) {
        return ReceptionDto.with()
            .withOrganisationNumber(new OrganisationNumber(dataTable.get("organisationNumber")))
            .withSubmitterId(new PersonId(dataTable.get("submitterId")))
            .withCategory(TaxCategory.valueOf(dataTable.get("taxCategory")))
            .withYear(Integer.parseInt(dataTable.get("year")))
            .withTaxationPeriodType(TaxationPeriodType.valueOf(dataTable.get("taxationPeriod")))
            .withTimeOfSubmission(LocalDateTime.parse(dataTable.get("timeOfSubmission")))
            .withVatLines(getVATLines(dataTable))
            .build();
    }

    public static VATLines getVATLines(Map<String, String> dataTable) {
        List<VATLine> vatLines = new ArrayList<>();
        int lineIndex = 0;
        while (
            dataTable.containsKey(vatLinePrefix(lineIndex) + ".vatCode") &&
                dataTable.containsKey(vatLinePrefix(lineIndex) + ".amount")
        ) {
            vatLines.add(
                new VATLine(
                    Integer.parseInt(dataTable.get(vatLinePrefix(lineIndex) + ".vatCode")),
                    Double.parseDouble(dataTable.get(vatLinePrefix(lineIndex) + ".amount"))
                )
            );
            lineIndex++;
        }
        return new VATLines(vatLines);
    }

    private static String vatLinePrefix(Integer lineIndex) {
        return "vatLines.vatLine[" + lineIndex + "]";
    }
}
