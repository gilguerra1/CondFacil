ALTER TABLE unit
ADD CONSTRAINT uk_unit_number_block
UNIQUE (number, block_id);