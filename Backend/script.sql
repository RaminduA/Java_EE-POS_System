DROP DATABASE IF EXISTS Company;
CREATE DATABASE IF NOT EXISTS Company;
USE Company;

DROP TABLE IF EXISTS Customer;
CREATE TABLE IF NOT EXISTS Customer(
    customerId  VARCHAR(8) NOT NULL,
    name VARCHAR(20),
    address VARCHAR(45),
    contact VARCHAR(15),
    PRIMARY KEY(customerId)
);
SELECT * FROM Customer;
DESC Customer;

DROP TABLE IF EXISTS Item;
CREATE TABLE IF NOT EXISTS Item(
    itemCode  VARCHAR(8) NOT NULL,
    name VARCHAR(20),
    price DECIMAL(8,2),
    quantity INT,
    PRIMARY KEY(itemCode)
);
SELECT * FROM Item;
DESC Item;

DROP TABLE IF EXISTS `Order`;
CREATE TABLE IF NOT EXISTS `Order`(
    orderId  VARCHAR(8) NOT NULL,
    customerId VARCHAR(8) NOT NULL,
    date DATE,
    time VARCHAR(13),
    cost DECIMAL(8,2),
    PRIMARY KEY(orderId),
    FOREIGN KEY (customerId)
        REFERENCES Customer(customerId)
        ON DELETE CASCADE
);
SELECT * FROM `Order`;
DESC `Order`;

INSERT INTO Item VALUES ('I-000001', 'Butter', 450.00, 25);