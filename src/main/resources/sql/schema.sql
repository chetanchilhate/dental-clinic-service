DROP TABLE IF EXISTS t_bill_lines;

DROP TABLE IF EXISTS t_bills;

DROP TABLE IF EXISTS t_patients;

DROP TABLE IF EXISTS t_doctors;

DROP TABLE IF EXISTS t_treatments;

DROP TABLE IF EXISTS t_clinics;

CREATE TABLE t_clinics
(
    id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE t_treatments
(
    id        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    fee       DOUBLE       NOT NULL DEFAULT 100.00 CHECK ( fee >= 100.00 ),
    clinic_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    KEY fk_clinic_treatment (clinic_id),
    CONSTRAINT fk_clinic_treatment FOREIGN KEY (clinic_id) REFERENCES t_clinics (id)
);

CREATE TABLE t_doctors
(
    id            INT UNSIGNED NOT NULL AUTO_INCREMENT,
    email         VARCHAR(100) NOT NULL,
    first_name    VARCHAR(100) NOT NULL,
    middle_name   VARCHAR(100) NOT NULL DEFAULT '',
    last_name     VARCHAR(100) NOT NULL,
    qualification VARCHAR(255) NOT NULL,
    clinic_id     INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    KEY fk_clinic_doctor (clinic_id),
    CONSTRAINT fk_clinic_doctor FOREIGN KEY (clinic_id) REFERENCES t_clinics (id)
);

CREATE TABLE t_patients
(
    id          INT UNSIGNED                     NOT NULL AUTO_INCREMENT,
    first_name  VARCHAR(100)                     NOT NULL,
    middle_name VARCHAR(100)                     NOT NULL DEFAULT '',
    last_name   VARCHAR(100)                     NOT NULL,
    age         TINYINT UNSIGNED                 NOT NULL CHECK ( 10 < age < 100 ),
    sex         ENUM ('MALE', 'FEMALE', 'OTHER') NOT NULL,
    mobile      VARCHAR(10)                      NOT NULL DEFAULT '',
    problem     VARCHAR(1000)                    NOT NULL DEFAULT '',
    doctor_id   INT UNSIGNED                     NOT NULL,
    PRIMARY KEY (id),
    KEY fk_doctor_patient (doctor_id),
    CONSTRAINT fk_doctor_patient FOREIGN KEY (doctor_id) REFERENCES t_doctors (id)
);

CREATE TABLE t_bills
(
    id               INT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_date_time TIMESTAMP    NOT NULL DEFAULT NOW(),
    total            DOUBLE       NOT NULL,
    patient_id       INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    KEY fk_patient_bill (patient_id),
    CONSTRAINT fk_patient_bill FOREIGN KEY (patient_id) REFERENCES t_patients (id)
) AUTO_INCREMENT = 1001;

CREATE TABLE t_bill_lines
(
    id        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    treatment VARCHAR(100) NOT NULL,
    fee       DOUBLE       NOT NULL,
    bill_id   INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    KEY fk_bill_line (bill_id),
    CONSTRAINT fk_bill_line FOREIGN KEY (bill_id) REFERENCES t_bills (id)
);
