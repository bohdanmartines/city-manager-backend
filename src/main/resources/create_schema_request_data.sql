-- Recreate the schema
DROP SCHEMA IF EXISTS request_data CASCADE;
CREATE SCHEMA request_data;

CREATE TABLE request_data.status
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE request_data.request
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    description VARCHAR      NOT NULL,
    creator_id  BIGINT       NOT NULL REFERENCES user_data.user (id),
    status_id   BIGINT       NOT NULL REFERENCES request_data.status (id),
    assignee_id BIGINT                      DEFAULT NULL REFERENCES user_data.user (id),
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE request_data.request_vote
(
    id         BIGSERIAL PRIMARY KEY,
    request_id BIGINT NOT NULL REFERENCES request_data.request (id),
    voter_id   BIGINT NOT NULL REFERENCES user_data.user (id)
);

CREATE TABLE request_data.request_message
(
    id         BIGSERIAL PRIMARY KEY,
    request_id BIGINT  NOT NULL REFERENCES request_data.request (id),
    message    VARCHAR NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE request_data.request_history
(
    id         BIGSERIAL PRIMARY KEY,
    request_id BIGINT  NOT NULL REFERENCES request_data.request (id),
    event      VARCHAR NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Insert test data
INSERT INTO request_data.status (name)
VALUES ('OPEN');
INSERT INTO request_data.status (name)
VALUES ('IN_PROGRESS');
INSERT INTO request_data.status (name)
VALUES ('RESOLVED');
INSERT INTO request_data.status (name)
VALUES ('DISCARDED');
