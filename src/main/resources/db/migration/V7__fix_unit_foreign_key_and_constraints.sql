ALTER TABLE unit DROP CONSTRAINT fk_block;

ALTER TABLE unit
ADD CONSTRAINT fk_unit_block
FOREIGN KEY (block_id) REFERENCES block(id) ON DELETE CASCADE;

ALTER TABLE unit
ADD CONSTRAINT uk_unit_number_block
UNIQUE (number, block_id);