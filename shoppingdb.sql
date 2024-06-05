-- Create the database
CREATE DATABASE shopping_system;

-- Use the database
USE shopping_system;

-- Drop the order_products table
DROP TABLE IF EXISTS order_products;

-- Drop the orders table
DROP TABLE IF EXISTS orders;

-- Drop the products table
DROP TABLE IF EXISTS products;

-- Drop the users table
DROP TABLE IF EXISTS users;


-- Create the users table with role
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Create the products table
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    description TEXT,
    brand VARCHAR(255),
    model VARCHAR(255),
    warranty_period INT,
    size VARCHAR(50),
    color VARCHAR(50),
    material VARCHAR(255)
);

-- Create the orders table
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_price DOUBLE NOT NULL,
);

-- Create the order_products table to link orders and products
CREATE TABLE order_products (
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
