CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_uuid UUID NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    person_id BIGINT UNIQUE,

    CONSTRAINT fk_user_person
        FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE
);