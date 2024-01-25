package com.bdd.workshop.type;

public enum TaxCategory {
    NORMAL,
    DELTA;

    @Override
    public String toString() {
        return name();
    }
}
