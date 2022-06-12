INSERT INTO t_clinics (id, name) VALUES (1, 'Sharda Dental Clinic');
INSERT INTO t_clinics (id, name) VALUES (2, 'Smart Dental Clinic');
INSERT INTO t_clinics (id, name) VALUES (3, 'Sonal Dental Clinic');

INSERT INTO t_treatments (id, name, fee, clinic_id) VALUES (1, 'Regular Checkup', 100.00, 1);
INSERT INTO t_treatments (id, name, fee, clinic_id) VALUES (2, 'Root Canal', 2500.00, 1);

INSERT INTO t_doctors (email, first_name, last_name, qualification, clinic_id)
VALUES ('atul@gmail.com', 'Atul', 'Kenjale', 'BDS', 1);

INSERT INTO t_patients (first_name, last_name, age, sex, doctor_id)
VALUES ('Chetan', 'Chilhate', 29, 'MALE', 1);
