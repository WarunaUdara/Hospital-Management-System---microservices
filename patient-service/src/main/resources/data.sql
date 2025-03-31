-- Ensure the 'patient' table exists
CREATE TABLE IF NOT EXISTS patient
(
    id                UUID PRIMARY KEY,
    name              VARCHAR(255)        NOT NULL,
    email             VARCHAR(255) UNIQUE NOT NULL,
    phone             VARCHAR(255)        NOT NULL,
    address           VARCHAR(255)        NOT NULL,
    birth_date        DATE                NOT NULL,
    registration_date DATE                NOT NULL,
    gender            VARCHAR(50)
    );

-- Insert well-known UUIDs for specific patients
INSERT INTO patient (id, name, email, phone, address, birth_date, registration_date, gender)
SELECT '123e4567-e89b-12d3-a456-426614174000',
       'John Doe',
       'john.doe@example.com',
       '555-123-4567',
       '123 Main St, Springfield',
       '1985-06-15',
       '2024-01-10',
       'Male'
    WHERE NOT EXISTS (SELECT 1
                  FROM patient
                  WHERE id = '123e4567-e89b-12d3-a456-426614174000');

INSERT INTO patient (id, name, email, phone, address, birth_date, registration_date, gender)
SELECT '123e4567-e89b-12d3-a456-426614174001',
       'Jane Smith',
       'jane.smith@example.com',
       '555-234-5678',
       '456 Elm St, Shelbyville',
       '1990-09-23',
       '2023-12-01',
       'Female'
    WHERE NOT EXISTS (SELECT 1
                  FROM patient
                  WHERE id = '123e4567-e89b-12d3-a456-426614174001');

INSERT INTO patient (id, name, email, phone, address, birth_date, registration_date, gender)
SELECT '123e4567-e89b-12d3-a456-426614174002',
       'Alice Johnson',
       'alice.johnson@example.com',
       '555-345-6789',
       '789 Oak St, Capital City',
       '1978-03-12',
       '2022-06-20',
       'Female'
    WHERE NOT EXISTS (SELECT 1
                  FROM patient
                  WHERE id = '123e4567-e89b-12d3-a456-426614174002');

INSERT INTO patient (id, name, email, phone, address, birth_date, registration_date, gender)
SELECT '123e4567-e89b-12d3-a456-426614174003',
       'Bob Brown',
       'bob.brown@example.com',
       '555-456-7890',
       '321 Pine St, Springfield',
       '1982-11-30',
       '2023-05-14',
       'Male'
    WHERE NOT EXISTS (SELECT 1
                  FROM patient
                  WHERE id = '123e4567-e89b-12d3-a456-426614174003');

INSERT INTO patient (id, name, email, phone, address, birth_date, registration_date, gender)
SELECT '123e4567-e89b-12d3-a456-426614174004',
       'Emily Davis',
       'emily.davis@example.com',
       '555-567-8901',
       '654 Maple St, Shelbyville',
       '1995-02-05',
       '2024-03-01',
       'Female'
    WHERE NOT EXISTS (SELECT 1
                  FROM patient
                  WHERE id = '123e4567-e89b-12d3-a456-426614174004');

-- Insert additional patients with well-known UUIDs
INSERT INTO patient (id, name, email, phone, address, birth_date, registration_date, gender)
SELECT '223e4567-e89b-12d3-a456-426614174005',
       'Michael Green',
       'michael.green@example.com',
       '555-678-9012',
       '987 Cedar St, Springfield',
       '1988-07-25',
       '2024-02-15',
       'Male'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '223e4567-e89b-12d3-a456-426614174005');

INSERT INTO patient (id, name, email, phone, address, birth_date, registration_date, gender)
SELECT '223e4567-e89b-12d3-a456-426614174006',
       'Sarah Taylor',
       'sarah.taylor@example.com',
       '555-789-0123',
       '123 Birch St, Shelbyville',
       '1992-04-18',
       '2023-08-25',
       'Female'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '223e4567-e89b-12d3-a456-426614174006');

INSERT INTO patient (id, name, email, phone, address, birth_date, registration_date, gender)
SELECT '223e4567-e89b-12d3-a456-426614174007',
       'David Wilson',
       'david.wilson@example.com',
       '555-890-1234',
       '456 Ash St, Capital City',
       '1975-01-11',
       '2022-10-10',
       'Male'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '223e4567-e89b-12d3-a456-426614174007');