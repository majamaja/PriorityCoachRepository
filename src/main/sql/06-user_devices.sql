CREATE TABLE user_devices
(
    installation_id             UUID        PRIMARY KEY         DEFAULT uuid_generate_v4(),
    user_id                     UUID        NOT NULL
                                            REFERENCES users(id) ON DELETE CASCADE,
    type                        VARCHAR     NOT NULL,
    token                       VARCHAR     NOT NULL,
    created_on                  TIMESTAMP   NOT NULL            DEFAULT now(),

    CONSTRAINT user_devices_unique_user_token
        UNIQUE (user_id, token)
);
