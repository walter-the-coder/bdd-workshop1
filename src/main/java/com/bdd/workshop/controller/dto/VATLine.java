package com.bdd.workshop.controller.dto;

public record VATLine(
    VATCode vatCode,
    Double amount
) { }
