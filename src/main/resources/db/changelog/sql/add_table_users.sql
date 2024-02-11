CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(150),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(150),
    path_to_icon VARCHAR(200),
    registration_time TIMESTAMP,
    last_updated_by BIGINT,
    last_update_time TIMESTAMP,
    deleted_by BIGINT,
    deletion_time TIMESTAMP
)