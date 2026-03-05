CREATE TABLE apartment_block (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    condominium_id BIGINT NOT NULL,

    CONSTRAINT fk_block_condominium
        FOREIGN KEY (condominium_id)
        REFERENCES condominium (id)
        ON DELETE CASCADE
);