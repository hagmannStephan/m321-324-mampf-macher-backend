-- Active: 1766042947393@@postgresdb@5432@postgres@recipe_schema
-- Insert Preferences
INSERT INTO preferences (name) VALUES 
('Vegetarian'),
('Gluten-Free'),
('Vegan'),
('Dairy-Free');

-- Insert Recipes
INSERT INTO recipe (name, description, recipe_url) VALUES
('Chocolate Cake', 'Rich and moist chocolate cake', 'https://example.com/chocolate-cake'),
('Pancakes', 'Fluffy breakfast pancakes', 'https://example.com/pancakes'),
('Vegan Stir Fry', 'Quick and healthy vegan stir fry', 'https://example.com/vegan-stir-fry'),
('Gluten-Free Bread', 'Soft gluten-free bread', 'https://example.com/gluten-free-bread');

-- Insert Ingredients
INSERT INTO ingredients (name) VALUES 
('flour'),
('sugar'),
('eggs'),
('butter'),
('soy sauce'),
('rice'),
('tofu'),
('almond milk');

-- Link Ingredients to Recipes
INSERT INTO ingredients_recipe (ingredients_id, recipe_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1),  -- flour, sugar, eggs, butter → Chocolate Cake
(1, 2), (2, 2), (3, 2),          -- flour, sugar, eggs → Pancakes
(5, 3), (6, 3), (7, 3),          -- soy sauce, rice, tofu → Vegan Stir Fry
(1, 4), (8, 4);                  -- flour, almond milk → Gluten-Free Bread

-- Link Preferences to Recipes
INSERT INTO preferences_recipe (preferences_id, recipe_id) VALUES
(1, 1), (2, 1),  -- Vegetarian, Gluten-Free → Chocolate Cake
(1, 2),          -- Vegetarian → Pancakes
(3, 3),          -- Vegan → Vegan Stir Fry
(2, 4),         -- Gluten-Free → Gluten-Free Bread
(4, 3),
(4, 4);