package com.bdd.workshop.type;

public enum TaxationPeriodType {
    JAN_FEB,
    MAR_APR,
    MAY_JUN,
    JUL_AUG,
    SEP_OCT,
    NOV_DEC;

    @Override
    public String toString() {
        return name();
    }
}
