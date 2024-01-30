package com.bdd.workshop.controller.dto;

import java.util.ArrayList;
import java.util.List;

public record VATLines(List<VATLine> vatLines) {
    public VATLines {
        if (vatLines == null) {
            vatLines = new ArrayList<>();
        }
    }
}
