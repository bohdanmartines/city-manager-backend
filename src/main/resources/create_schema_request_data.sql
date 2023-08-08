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
VALUES ('CREATED');
INSERT INTO request_data.status (name)
VALUES ('IN_PROGRESS');
INSERT INTO request_data.status (name)
VALUES ('RESOLVED');
INSERT INTO request_data.status (name)
VALUES ('DISCARDED');

INSERT INTO request_data.request (title, description, creator_id, status_id)
VALUES ('Plan trees in the central park', 'The central park needs more trees!', 1, 1);
INSERT INTO request_data.request (title, description, creator_id, status_id, assignee_id)
VALUES ('Add streen lights to Main street', 'It''s noticeably dark on the Main street after the sunset. Could you add more lights please?', 1, 1, 1);


INSERT INTO request_data.request_vote (request_id, voter_id)
VALUES (1, 1);
INSERT INTO request_data.request_vote (request_id, voter_id)
VALUES (1, 2);

INSERT INTO request_data.request_message (request_id, message)
VALUES (1, 'Test message');

INSERT INTO request_data.request_history (request_id, event)
VALUES (1, 'Changed assignee to Jennifer');