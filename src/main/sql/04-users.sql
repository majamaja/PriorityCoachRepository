CREATE EXTENSION "uuid-ossp";

CREATE TABLE users (
    id                      UUID            PRIMARY KEY     DEFAULT uuid_generate_v4(),
    email                   VARCHAR         UNIQUE NOT NULL,
    phone_number            VARCHAR,
    name                    VARCHAR         NOT NULL,
    password                VARCHAR,
    created_on              TIMESTAMP       NOT NULL        DEFAULT now(),
    last_modified           TIMESTAMP       NOT NULL        DEFAULT now(),
    last_password_change    TIMESTAMP       NOT NULL        DEFAULT now(),
    last_login              TIMESTAMP
);

INSERT INTO users (email, name, password)
    VALUES ('zhilkov@futurist-labs.com', 'Gencho Zhilkov', 'd404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db');
