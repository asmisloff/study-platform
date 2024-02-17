BEGIN TRANSACTION;

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(300)
);

CREATE TABLE learning_statuses (
    id BIGINT PRIMARY KEY,
    name VARCHAR(16)
);

CREATE TABLE file_types (
    id BIGINT PRIMARY KEY,
    name VARCHAR(12)
);

CREATE TABLE roles (
    id BIGINT PRIMARY KEY,
    name VARCHAR(12)
);

CREATE TABLE files (
    id BIGSERIAL PRIMARY KEY,
    client_name VARCHAR(100),
    actual_name VARCHAR(16),
    type_id BIGINT,
    thumbnail VARCHAR(16),
    bucket VARCHAR(16),
    FOREIGN KEY (type_id) REFERENCES file_types(id)
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    pwd VARCHAR(150) NOT NULL UNIQUE,
    first_name VARCHAR(150) NOT NULL,
    last_name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(50),
    avatar_id BIGINT,
    registration_time TIMESTAMP,
    last_update_time TIMESTAMP,
    deletion_time TIMESTAMP,
    last_updated_user_id BIGINT,
    deleted_user_id BIGINT,
    FOREIGN KEY (avatar_id) REFERENCES files(id)
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    created_user_id BIGINT,
    last_updated_user_id BIGINT,
    deleted_user_id BIGINT,
    creation_time TIMESTAMP,
    last_update_time TIMESTAMP,
    deletion_time TIMESTAMP,
    category_id BIGINT,
    estimated_duration INTEGER,
    tag VARCHAR(32),
    FOREIGN KEY (created_user_id) REFERENCES users(id),
    FOREIGN KEY (last_updated_user_id) REFERENCES users(id),
    FOREIGN KEY (deleted_user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE modules (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100),
    description TEXT,
    created_user_id BIGINT,
    last_updated_user_id BIGINT,
    deleted_user_id BIGINT,
    creation_time TIMESTAMP,
    last_update_time TIMESTAMP,
    deletion_time TIMESTAMP,
    course_id BIGINT,
    idx INTEGER,
    FOREIGN KEY (created_user_id) REFERENCES users(id),
    FOREIGN KEY (last_updated_user_id) REFERENCES users(id),
    FOREIGN KEY (deleted_user_id) REFERENCES users(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100),
    description TEXT,
    created_user_id BIGINT,
    last_updated_user_id BIGINT,
    deleted_user_id BIGINT,
    creation_time TIMESTAMP,
    last_update_time TIMESTAMP,
    deletion_time TIMESTAMP,
    content TEXT,
    module_id BIGINT,
    idx INTEGER,
    FOREIGN KEY (created_user_id) REFERENCES users(id),
    FOREIGN KEY (last_updated_user_id) REFERENCES users(id),
    FOREIGN KEY (deleted_user_id) REFERENCES users(id),
    FOREIGN KEY (module_id) REFERENCES modules(id)
);

CREATE TABLE users_to_courses (
    user_id BIGINT,
    course_id BIGINT,
    start_time TIMESTAMP,
    completion_time TIMESTAMP,
    score INTEGER,
    course_rating SMALLINT,
    review TEXT,
    PRIMARY KEY (user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE users_to_lessons (
    user_id BIGINT,
    lesson_id BIGINT,
    status_id BIGINT,
    start_time TIMESTAMP,
    completion_time TIMESTAMP,
    PRIMARY KEY (user_id, lesson_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (lesson_id) REFERENCES lessons(id),
    FOREIGN KEY (status_id) REFERENCES learning_statuses(id)
);

COMMIT;