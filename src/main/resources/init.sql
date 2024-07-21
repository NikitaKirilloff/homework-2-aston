CREATE TABLE IF NOT EXISTS order_details(
    id SERIAL PRIMARY KEY,
    order_status VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL
    );

CREATE TABLE IF NOT EXISTS products(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity BIGINT NOT NULL DEFAULT 0,
    available BOOLEAN NOT NULL,
    order_detail_id BIGINT REFERENCES order_details(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS products_categories(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL
    );

-- Таблица связи Product и ProductCategory (ManyToMany)
CREATE TABLE IF NOT EXISTS products_products_categories(
   product_id BIGINT,
   category_id BIGINT,
   PRIMARY KEY(product_id, category_id),
   FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
   FOREIGN KEY(category_id) REFERENCES products_categories(id) ON DELETE CASCADE
   );

CREATE TABLE IF NOT EXISTS order_approval(
    id SERIAL PRIMARY  KEY,
    order_detail_id  BIGINT REFERENCES order_details(id) UNIQUE
    );

INSERT INTO products_categories (name, type)
VALUES ('Холодные', 'Напитки'),
       ('Горячие', 'Напитки'),
       ('Горячие', 'Закуски'),
       ('Холодные', 'Закуски'),
       ('Пирожное', 'Десерты'),
       ('Горячие', 'Основное_блюдо');


INSERT INTO order_details (order_status, total_amount) VALUES
   ('Принят', 545.00),
   ('Готов', 180.00),
   ('Готовится', 240.00),
    ('Готов', 100.00);


INSERT INTO products (name, price, quantity, available, order_detail_id)
VALUES ('Кока-Кола', 85, 10, TRUE, 1),
       ('Лобио', 360, 6, TRUE, 1),
       ('Пепси', 80, 5, TRUE, 3),
       ('Картофель фри', 160, 7, TRUE, 3),
       ('Бургер', 180, 10, TRUE, 2),
       ('Чай', 100, 20, TRUE, 4),
       ('Кофе', 120, 20, TRUE, 4);

INSERT INTO products_products_categories (product_id, category_id)
VALUES (1, 1),
       (2, 1),
       (3, 3),
       (4, 3),
       (5, 3),
       (6, 2);




