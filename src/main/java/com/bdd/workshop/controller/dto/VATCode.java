package com.bdd.workshop.controller.dto;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VATCode {
    VAT_CODE_1(1),
    VAT_CODE_2(2),
    VAT_CODE_3(3),
    VAT_CODE_4(4),
    VAT_CODE_5(5),
    VAT_CODE_6(6),
    VAT_CODE_7(7),
    VAT_CODE_8(8),
    VAT_CODE_9(9),
    VAT_CODE_10(10);

    @JsonValue
    public final Integer code;

    VATCode(Integer code) {
        this.code = code;
    }

    public static VATCode fromCode(Integer code) {
        return Arrays.stream(VATCode.values()).filter(it -> Objects.equals(it.code, code)).findFirst().get();
    }
}
