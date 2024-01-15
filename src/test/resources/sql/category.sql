DELETE FROM tb_order_product;
DELETE FROM tb_order;
DELETE FROM tb_product;
DELETE FROM tb_category;

INSERT INTO tb_category (id, name, description, dh_insert)
VALUES (1, 'Bebidas1', 'Consumível líquido que possa ser comprado para consumo, como Água, Refrigerante, Cerveja entre outros.', now())
ON CONFLICT DO NOTHING;

INSERT INTO tb_category (id, name, description, dh_insert)
VALUES (2, 'Sobremesa1', 'Algum doce, pode incluir bolos, tortas, sorvetes, pudins, entre outros.', now())
ON CONFLICT DO NOTHING;

INSERT INTO tb_category (id, name, description, dh_insert)
VALUES (3, 'Lanche1', 'Refeição pronta para consumo que possa ser comprada como Hambuguer, Pizza, Sanduiche entre outros.', now())
ON CONFLICT DO NOTHING;

INSERT INTO tb_category (id, name, description, dh_insert)
VALUES (4, 'Acompanhamento1', 'Todo e qualquer alimento de pequena média proporção que possa ser consumido com outras refeições.', now())
ON CONFLICT DO NOTHING;

INSERT INTO tb_product (id, name, price, quantity, id_category)
VALUES (1, 'Suco', 19.99, 10, 1),
(2, 'Sorvete', 29.99, 5, 2),
(3, 'Hamburguer', 39.99, 15, 3),
(4, 'Batata Frita', 9.99, 15, 4) ON CONFLICT DO NOTHING;