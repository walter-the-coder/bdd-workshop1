CREATE TABLE transactions
(
    id                 BIGSERIAL PRIMARY KEY               NOT NULL,
    organisationNumber TEXT                                NOT NULL,
    submitterId        TEXT                                NOT NULL,
    category           TEXT                                NOT NULL,
    year               INT                                 NOT NULL,
    taxationPeriodType TEXT                                NOT NULL,
    timeOfSubmission   TIMESTAMP,
    created            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status             TEXT                                NOT NULL,
    vatLines           JSONB                               NOT NULL
);