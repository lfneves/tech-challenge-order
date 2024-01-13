DELETE FROM tb_order_product;
DELETE FROM tb_order;
DELETE FROM tb_product;

INSERT INTO tb_product (id, name, price, quantity, id_category)
VALUES (1, 'Suco', 19.99, 10, 1),
(2, 'Sorvete', 29.99, 5, 2),
(3, 'Hamburguer', 39.99, 15, 3),
(4, 'Batata Frita', 9.99, 15, 4);