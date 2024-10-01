-- Recreate the schema
DROP SCHEMA IF EXISTS user_data CASCADE;
CREATE SCHEMA user_data;

CREATE TABLE user_data.role
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE user_data.user
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(100)        NOT NULL,
    name       VARCHAR(50)         NOT NULL,
    surname    VARCHAR(50)         NOT NULL,
    enabled    BOOLEAN             NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_data.user_role
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES user_data.user (id),
    role_id BIGINT NOT NULL REFERENCES user_data.role (id)
);
ALTER TABLE user_data.user_role
    ADD CONSTRAINT unique_user_role UNIQUE (user_id, role_id);

-- Insert test data
INSERT INTO user_data.role (name)
VALUES ('CITIZEN');
INSERT INTO user_data.role (name)
VALUES ('EXECUTOR');
INSERT INTO user_data.role (name)
VALUES ('ADMIN');
