package com.bdd.workshop.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdd.workshop.controller.dto.ReceptionDto;
import com.bdd.workshop.controller.dto.RetrievingDto;
import com.bdd.workshop.repository.TransactionRepository;

@RestController
@RequestMapping("/api/retrieving")
public class RetrievingController {
    private final TransactionRepository transactionRepository;

    public RetrievingController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping(value = "/unprocessed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RetrievingDto> handleData() {
        List<ReceptionDto> unprocessedData = transactionRepository.getUnprocessedData();
        return ResponseEntity.ok(new RetrievingDto(unprocessedData));
    }
}
