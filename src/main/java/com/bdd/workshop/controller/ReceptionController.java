package com.bdd.workshop.controller;

import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.ReceptionResponse;
import com.bdd.workshop.controller.dto.ValidationResponse;
import com.bdd.workshop.service.InputValidationService;
import com.bdd.workshop.service.ReceptionService;

@RestController
@RequestMapping("/api/reception")
public class ReceptionController {

    private final ReceptionService receptionService;
    private final InputValidationService inputValidationService;

    public ReceptionController(
        ReceptionService receptionService,
        InputValidationService inputValidationService
    ) {
        this.receptionService = receptionService;
        this.inputValidationService = inputValidationService;
    }

    @GetMapping(value = "/validateName/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> validate(
        @PathVariable String name
    ) {
        if (Objects.equals(name, "John Deer")) {
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.badRequest().body(false);
    }

    @PostMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidationResponse> validate(
        @RequestBody ReceptionDto data
    ) {
        ValidationResponse response = inputValidationService.validate(data);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceptionResponse> handleData(
        @RequestBody ReceptionDto data
    ) {
        ReceptionResponse response = receptionService.handleReceivedData(data);
        return ResponseEntity.ok(response);
    }
}
