DROP SCHEMA IF EXISTS store_items_schema CASCADE;
CREATE SCHEMA store_items_schema;

SET search_path TO store_items_schema;

CREATE TABLE store_items_schema.items (
  id    BIGSERIAL PRIMARY KEY,
  name  VARCHAR(64) NOT NULL UNIQUE,
  price DOUBLE PRECISION NOT NULL,
  stock INTEGER NOT NULL
);
