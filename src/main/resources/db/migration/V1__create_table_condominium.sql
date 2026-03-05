CREATE TABLE condominium (
    id BIGSERIAL PRIMARY KEY,
    condominium_uuid UUID NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    legal_name VARCHAR(255),
    tax_id VARCHAR(18) UNIQUE,
    address VARCHAR(255),
    number VARCHAR(20),
    neighborhood VARCHAR(100),
    city VARCHAR(100),
    state VARCHAR(2),
    zip_code VARCHAR(10),
    concierge_phone VARCHAR(20),
    management_email VARCHAR(255)
);