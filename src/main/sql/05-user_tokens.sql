CREATE TABLE user_tokens (
    token           VARCHAR     PRIMARY KEY         DEFAULT uuid_generate_v4(),
    user_id         UUID        NOT NULL
                                REFERENCES users (id) ON DELETE CASCADE,
    type            VARCHAR(80) NOT NULL,
    expiry_date     TIMESTAMP   NOT NULL,
    is_used         BOOL        NOT NULL            DEFAULT FALSE
);
