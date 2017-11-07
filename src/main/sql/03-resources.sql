CREATE TABLE resources (
    id              UUID        PRIMARY KEY     DEFAULT uuid_generate_v4(),
    created_on      TIMESTAMP   NOT NULL        DEFAULT now(),
    mime_type       VARCHAR(40) NOT NULL,
    content         BYTEA       NOT NULL
);
