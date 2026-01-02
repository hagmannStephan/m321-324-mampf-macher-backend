INSERT INTO recipe_schema.preferences (name) VALUES
('Vegetarian'),
('Gluten-Free'),
('Vegan'),
('Dairy-Free');

INSERT INTO recipe_schema.recipe (name, description, recipe_url) VALUES
('Chocolate Cake', 'Rich and moist chocolate cake', 'https://www.bettybossi.ch/de/rezepte/rezept/grossmutters-schokoladekuchen-10004646/'),
('Pancakes', 'Fluffy breakfast pancakes', 'https://www.bettybossi.ch/de/rezepte/rezept/pancakes-10007372/'),
('Vegan Stir Fry', 'Quick and healthy vegan stir fry', 'https://www.noracooks.com/vegan-stir-fry/'),
('Gluten-Free Bread', 'Soft gluten-free bread', 'https://www.mamaknowsglutenfree.com/easy-gluten-free-bread/');

INSERT INTO recipe_schema.ingredients (name) VALUES
('flour'),
('sugar'),
('eggs'),
('butter'),
('soy sauce'),
('rice'),
('tofu'),
('almond milk');

INSERT INTO recipe_schema.ingredients_recipe (ingredients_id, recipe_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1),
(1, 2), (2, 2), (3, 2),
(5, 3), (6, 3), (7, 3),
(1, 4), (8, 4);

INSERT INTO recipe_schema.preferences_recipe (preferences_id, recipe_id) VALUES
(1, 1), (2, 1),
(1, 2),
(3, 3),
(2, 4),
(4, 3),
(4, 4);
