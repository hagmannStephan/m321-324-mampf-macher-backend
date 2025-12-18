-- Active: 1766042947393@@postgresdb@5432@postgres@public

-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS recipe_schema;

-- Set search path to use the new schema
SET search_path TO recipe_schema;

-- 1. Create tables
CREATE TABLE preferences (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE recipe (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(128),
    recipe_url VARCHAR(256)
);

CREATE TABLE ingredients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

-- 2. Create junction tables (many-to-many)
CREATE TABLE preferences_recipe (
    preferences_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    PRIMARY KEY (preferences_id, recipe_id),
    FOREIGN KEY (preferences_id) REFERENCES preferences(id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE
);

CREATE TABLE ingredients_recipe (
    ingredients_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    PRIMARY KEY (ingredients_id, recipe_id),
    FOREIGN KEY (ingredients_id) REFERENCES ingredients(id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE
);