DROP TABLE IF EXISTS t_treatment;

DROP TABLE IF EXISTS t_clinic;

CREATE TABLE t_clinic
(
    id   INT          NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE t_treatment
(
    id        INT NOT NULL AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    charges   DOUBLE       DEFAULT 0.00,
    clinic_id INT NOT NULL,
    PRIMARY KEY (id),
    KEY fk_clinic_treatment (clinic_id),
    CONSTRAINT fk_clinic_treatment FOREIGN KEY (clinic_id) REFERENCES t_clinic (id)
);
