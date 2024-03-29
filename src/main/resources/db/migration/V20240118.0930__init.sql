CREATE TABLE TRANSACTIONS
(
    TRANSACTIONS_ID      NUMBER AUTO_INCREMENT       NOT NULL,
    ORGANISATION_NUMBER  VARCHAR2(50)                NOT NULL,
    SUBMITTER_TIN        VARCHAR2(50)                NOT NULL,
    CATEGORY             VARCHAR2(50)                NOT NULL,
    TAXATION_YEAR        NUMBER(4)                   NOT NULL,
    TAXATION_PERIOD_TYPE VARCHAR2(50)                NOT NULL,
    TIME_OF_SUBMISSION   TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    CREATED              TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    STATUS               VARCHAR2(50)                NOT NULL,
    VAT_LINES            JSON                        NOT NULL
);