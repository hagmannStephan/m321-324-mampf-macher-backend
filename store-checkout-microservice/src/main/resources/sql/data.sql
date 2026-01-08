SET search_path TO checkout_schema;

INSERT INTO orders (status, total) VALUES ('CREATED', 6.60);

INSERT INTO order_items (order_id, item_id, name, quantity, unit_price, line_total) VALUES
  (1, 1, 'Apple', 2, 0.80, 1.60),
  (1, 2, 'Banana', 5, 0.60, 3.00),
  (1, 4, 'Milk 1L', 1, 2.00, 2.00);
