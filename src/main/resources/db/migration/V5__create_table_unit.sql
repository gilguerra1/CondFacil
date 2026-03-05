CREATE TABLE unit (
    id SERIAL PRIMARY KEY,
    unit_uuid UUID NOT NULL UNIQUE,
    number VARCHAR(10) NOT NULL,
    floor INTEGER,
    type VARCHAR(20), 
    block_id INTEGER NOT NULL,
    CONSTRAINT fk_block FOREIGN KEY (block_id) REFERENCES apartment_block(id) ON DELETE CASCADE
);