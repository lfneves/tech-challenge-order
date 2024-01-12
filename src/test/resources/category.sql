DELETE FROM tb_order_product;
DELETE FROM tb_order;
DELETE FROM tb_product;
DELETE FROM public.tb_category;

INSERT INTO public.tb_category (id, name, description, dh_insert)
VALUES (1, 'Bebidas', 'Consumível líquido que possa ser comprado para consumo, como Água, Refrigerante, Cerveja entre outros.', now());

INSERT INTO public.tb_category (id, name, description, dh_insert)
VALUES (2, 'Sobremesa', 'Algum doce, pode incluir bolos, tortas, sorvetes, pudins, entre outros.', now());

INSERT INTO public.tb_category (id, name, description, dh_insert)
VALUES (3, 'Lanche', 'Refeição pronta para consumo que possa ser comprada como Hambuguer, Pizza, Sanduiche entre outros.', now());

INSERT INTO public.tb_category (id, name, description, dh_insert)
VALUES (4, 'Acompanhamento', 'Todo e qualquer alimento de pequena média proporção que possa ser consumido com outras refeições.', now());

INSERT INTO tb_product (id, name, price, quantity, id_category)
VALUES (1, 'Suco', 19.99, 10, 1),
(2, 'Sorvete', 29.99, 5, 2),
(3, 'Hamburguer', 39.99, 15, 3),
(4, 'Batata Frita', 9.99, 15, 4);