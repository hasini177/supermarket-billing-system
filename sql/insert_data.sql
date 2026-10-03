USE billing_system;
drop table inventory;
INSERT INTO inventory
    (product_id, product_name, price, stock)
VALUES
(101, 'Pen', 5.00, 100),
(102, 'Book', 20.00, 50),
(103, 'Pencil', 5.00, 100),
(104, 'Notebook', 40.00, 50),
(105, 'Eraser', 3.00, 80);

INSERT INTO inventory
    (product_id, product_name, price, stock)
VALUES
(106, 'Sharpener', 5.00, 60),
(107, 'Marker', 25.00, 40),
(108, 'Scale', 10.00, 70),
(109, 'Glue', 30.00, 35),
(110, 'Stapler', 60.00, 25),
(111, 'Staples', 15.00, 100),
(112, 'File', 35.00, 45),
(113, 'Folder', 20.00, 60),
(114, 'Highlighter', 20.00, 50),
(115, 'Calculator', 150.00, 20),
(116, 'Scissors', 45.00, 30),
(117, 'Tape', 20.00, 40),
(118, 'Paper Pack', 250.00, 15),
(119, 'Color Pencils', 80.00, 25),
(120, 'Sketch Pens', 70.00, 30);