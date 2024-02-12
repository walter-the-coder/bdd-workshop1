package com.bdd.workshop.controller.dto;

import java.util.List;

public record RetrievingDto(
    List<ReceptionDto> data
) {
}
