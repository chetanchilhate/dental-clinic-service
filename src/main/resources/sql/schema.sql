DROP TABLE IF EXISTS t_bill_lines;

DROP TABLE IF EXISTS t_bills;

DROP TABLE IF EXISTS t_patients;

DROP TABLE IF EXISTS t_doctors;

DROP TABLE IF EXISTS t_treatments;

DROP TABLE IF EXISTS t_clinics;

DROP TYPE IF EXISTS t_sex;

CREATE TABLE t_clinics
(
    id   SERIAL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE t_treatments
(
    id        SERIAL,
    name      VARCHAR(255) NOT NULL,
    fee       NUMERIC      NOT NULL,
    clinic_id INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_clinic_treatment FOREIGN KEY (clinic_id) REFERENCES t_clinics (id)
);

CREATE TABLE t_doctors
(
    id            SERIAL,
    email         VARCHAR(100) NOT NULL,
    first_name    VARCHAR(100) NOT NULL,
    middle_name   VARCHAR(100) NOT NULL DEFAULT '',
    last_name     VARCHAR(100) NOT NULL,
    qualification VARCHAR(255) NOT NULL,
    clinic_id     INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_clinic_doctor FOREIGN KEY (clinic_id) REFERENCES t_clinics (id)
);

CREATE TYPE t_sex AS ENUM ('MALE', 'FEMALE', 'OTHER');

CREATE TABLE t_patients
(
    id          SERIAL,
    first_name  VARCHAR(100)  NOT NULL,
    middle_name VARCHAR(100)  NOT NULL DEFAULT '',
    last_name   VARCHAR(100)  NOT NULL,
    age         SMALLINT      NOT NULL CHECK ( age > 10 ), CHECK ( age < 100 ),
    sex         T_SEX         NOT NULL,
    mobile      VARCHAR(10)   NOT NULL DEFAULT '',
    problem     VARCHAR(1000) NOT NULL DEFAULT '',
    doctor_id   INT           NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_doctor_patient FOREIGN KEY (doctor_id) REFERENCES t_doctors (id)
);

CREATE TABLE t_bills
(
    id               SERIAL,
    create_date_time TIMESTAMP NOT NULL DEFAULT NOW(),
    total            NUMERIC   NOT NULL,
    patient_id       INT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_patient_bill FOREIGN KEY (patient_id) REFERENCES t_patients (id)
);

CREATE TABLE t_bill_lines
(
    id        SERIAL,
    treatment VARCHAR(100) NOT NULL,
    fee       NUMERIC      NOT NULL,
    bill_id   INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_bill_line FOREIGN KEY (bill_id) REFERENCES t_bills (id)
);
