CREATE TABLE topics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150),
    description VARCHAR (150),
    created_user_id BIGINT,
    last_updated_by BIGINT,
    deleted_by BIGINT,
    creation_time TIMESTAMP,
    last_update_time TIMESTAMP,
    deletion_time TIMESTAMP,
    text TEXT
)