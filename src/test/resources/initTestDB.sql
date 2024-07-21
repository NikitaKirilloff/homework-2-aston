CREATE TABLE IF NOT EXISTS order_details(
              id SERIAL PRIMARY KEY,
              order_status VARCHAR(50) NOT NULL,
              total_amount DECIMAL(10, 2) NOT NULL
              );
CREATE TABLE IF NOT EXISTS products_categories(
              id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL,
              type VARCHAR(50) NOT NULL
              );
CREATE TABLE IF NOT EXISTS products(
              id SERIAL PRIMARY KEY,
              name VARCHAR(100) NOT NULL,
              price DECIMAL(10, 2) NOT NULL,
              quantity BIGINT NOT NULL DEFAULT 0,
              available BOOLEAN NOT NULL,
              order_detail_id BIGINT REFERENCES order_details(id) ON DELETE CASCADE
              );
CREATE TABLE IF NOT EXISTS products_products_categories(
              product_id BIGINT,
              category_id BIGINT,
              PRIMARY KEY(product_id, category_id),
              FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
              FOREIGN KEY(category_id) REFERENCES products_categories(id) ON DELETE CASCADE
              );
INSERT INTO products_categories (name, type)
VALUES ('Холодные', 'Напитки'),
       ('Горячие', 'Напитки'),
       ('Горячие', 'Закуски'),
       ('Холодные', 'Закуски'),
       ('Пирожное', 'Десерты'),
       ('Горячие', 'Основное_блюдо');