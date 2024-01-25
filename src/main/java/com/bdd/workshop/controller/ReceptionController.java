package com.bdd.workshop.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.service.ReceptionService;

@RestController
@RequestMapping("/api/reception")
public class ReceptionController {

    private final ReceptionService receptionService;

    public ReceptionController(ReceptionService receptionService) {
        this.receptionService = receptionService;
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public void handleData(
        @RequestBody ReceptionDto data
    ) {
        receptionService.handleReceivedData(data);
    }
}
