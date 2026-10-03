USE billing_system;

-- Find product using Product ID
SELECT product_id, product_name, price, stock
FROM inventory
WHERE product_id = ?;

-- Reduce stock after successful checkout
UPDATE inventory
SET stock = stock - ?
WHERE product_id = ?;

-- Create a new bill
INSERT INTO bills (subtotal, tax, total)
VALUES (?, ?, ?);

-- Add an item to the bill
INSERT INTO bill_items
    (bill_id, product_id, quantity, price, amount)
VALUES
    (?, ?, ?, ?, ?);