DROP SCHEMA IF EXISTS recipe_schema CASCADE;
CREATE SCHEMA recipe_schema;

CREATE TABLE recipe_schema.preferences (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE recipe_schema.recipe (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(128),
    recipe_url VARCHAR(256)
);

CREATE TABLE recipe_schema.ingredients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE recipe_schema.preferences_recipe (
    preferences_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    PRIMARY KEY (preferences_id, recipe_id),
    FOREIGN KEY (preferences_id)
        REFERENCES recipe_schema.preferences(id)
        ON DELETE CASCADE,
    FOREIGN KEY (recipe_id)
        REFERENCES recipe_schema.recipe(id)
        ON DELETE CASCADE
);

CREATE TABLE recipe_schema.ingredients_recipe (
    ingredients_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    PRIMARY KEY (ingredients_id, recipe_id),
    FOREIGN KEY (ingredients_id)
        REFERENCES recipe_schema.ingredients(id)
        ON DELETE CASCADE,
    FOREIGN KEY (recipe_id)
        REFERENCES recipe_schema.recipe(id)
        ON DELETE CASCADE
);
