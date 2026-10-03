USE billing_system;

CREATE TABLE inventory (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE bills (
    bill_id INT PRIMARY KEY AUTO_INCREMENT,
    bill_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    subtotal DOUBLE NOT NULL,
    tax DOUBLE NOT NULL,
    total DOUBLE NOT NULL
);

CREATE TABLE bill_items (
    bill_item_id INT PRIMARY KEY AUTO_INCREMENT,
    bill_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    amount DOUBLE NOT NULL,

    FOREIGN KEY (bill_id) REFERENCES bills(bill_id),
    FOREIGN KEY (product_id) REFERENCES inventory(product_id)
);