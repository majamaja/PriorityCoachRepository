BEGIN;
CREATE EXTENSION "uuid-ossp";


CREATE TABLE system_properties (
    name                        VARCHAR             UNIQUE NOT NULL,
    value                       VARCHAR
);

CREATE TABLE static_pages (
    id                          UUID                PRIMARY KEY         DEFAULT uuid_generate_v4(),
    created_at                  TIMESTAMP           NOT NULL            DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    updated_at                  TIMESTAMP           NOT NULL            DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    name                        VARCHAR(50)         UNIQUE NOT NULL,
    header                      VARCHAR(100)        NOT NULL,
    content                     TEXT                NOT NULL
);

CREATE TABLE resources (
    id                          UUID                PRIMARY KEY         DEFAULT uuid_generate_v4(),
    created_at                  TIMESTAMP           NOT NULL            DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    mime_type                   VARCHAR(40)         NOT NULL,
    content                     BYTEA               NOT NULL
);

CREATE TABLE admin_users (
    id                          UUID                PRIMARY KEY         DEFAULT uuid_generate_v4(),
    created_at                  TIMESTAMP           NOT NULL            DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    updated_at                  TIMESTAMP           NOT NULL            DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    last_password_change        TIMESTAMP           NOT NULL            DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    last_login                  TIMESTAMP,
    username                    VARCHAR             UNIQUE NOT NULL,
    password                    VARCHAR,
    name                        VARCHAR             NOT NULL,
    email                       VARCHAR             UNIQUE NOT NULL,
    is_blocked                  BOOLEAN             NOT NULL            DEFAULT FALSE
);

CREATE TABLE users (
    id                          UUID                PRIMARY KEY         DEFAULT uuid_generate_v4(),
    created_at                  TIMESTAMP           NOT NULL            DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    updated_at                  TIMESTAMP           NOT NULL            DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    last_password_change        TIMESTAMP           NOT NULL            DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    last_login                  TIMESTAMP,
    email                       VARCHAR             UNIQUE NOT NULL,
    password                    VARCHAR,
    name                        VARCHAR             NOT NULL
);

CREATE TABLE user_tokens (
    token                       VARCHAR             PRIMARY KEY         DEFAULT uuid_generate_v4(),
    user_id                     UUID                NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    type                        VARCHAR(80)         NOT NULL,
    expiry_date                 TIMESTAMP           NOT NULL,
    is_used                     BOOL                NOT NULL            DEFAULT FALSE
);

CREATE TABLE user_devices (
    installation_id             UUID        PRIMARY KEY                 DEFAULT uuid_generate_v4(),
    user_id                     UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type                        VARCHAR     NOT NULL,
    token                       VARCHAR     NOT NULL,
    created_at                  TIMESTAMP   NOT NULL                    DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),

    CONSTRAINT user_devices_unique_user_token
        UNIQUE (user_id, token)
);


-- Date & Time interval functions:
-- These assume that NULL values for start/end mean NO start/end for the interval.
CREATE OR REPLACE FUNCTION is_inside(t TIMESTAMP, startTime TIMESTAMP, entTime TIMESTAMP)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN (startTime IS NULL OR t >= startTime) AND (entTime IS NULL OR t <= entTime);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION is_inside(t DATE, startDate DATE, endDate DATE)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN (startDate IS NULL OR t >= startDate) AND (endDate IS NULL OR t <= endDate);
END;
$$ LANGUAGE plpgsql;


-- Function to calculate the distance btx. two points given their respective latitudes & longitudes
-- Source: https://www.movable-type.co.uk/scripts/latlong.html. Uses `Spherical Law of Cosines`.
-- Returns the distance in METERS. Accuracy ~ 3m/km.
CREATE OR REPLACE FUNCTION geo_distance(lat1 DOUBLE PRECISION, long1 DOUBLE PRECISION, lat2 DOUBLE PRECISION, long2 DOUBLE PRECISION)
RETURNS DOUBLE PRECISION AS $$
DECLARE
    R  CONSTANT DOUBLE PRECISION := 6371000;          -- mean Earth radius in meters
    DG CONSTANT DOUBLE PRECISION := PI() / 180;       -- 1 degree in radians
BEGIN
    RETURN ACOS(SIN(lat1 * DG) * SIN(lat2 * DG) + COS(lat1 * DG) * COS(lat2 * DG) * COS((long2 - long1) * DG)) * R;
END;
$$ LANGUAGE plpgsql;


-- Default admin password: 1234
INSERT INTO admin_users (name, email, username, password)
    VALUES ('admin', 'admin@futuristlabs.com', 'admin', 'd404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db');
COMMIT;
