INSERT INTO public.tb_category (name, description, dh_insert)
VALUES ('Bebidas', 'Consumível líquido que possa ser comprado para consumo, como Água, Refrigerante, Cerveja entre outros.', now());

INSERT INTO tb_category (name, description, dh_insert)
VALUES ('Sobremesa', 'Algum doce, pode incluir bolos, tortas, sorvetes, pudins, entre outros.', now());

INSERT INTO tb_category (name, description, dh_insert)
VALUES ('Lanche', 'Refeição pronta para consumo que possa ser comprada como Hambuguer, Pizza, Sanduiche entre outros.', now());

INSERT INTO tb_category (name, description, dh_insert)
VALUES ('Acompanhamento', 'Todo e qualquer alimento de pequena média proporção que possa ser consumido com outras refeições.', now());

INSERT INTO tb_address (id, city, dh_insert, postal_code, state, street)
VALUES ( 1, 'Springfield', now(), '12345', 'IL', '123 Main St');

INSERT INTO tb_client (cpf, dh_insert, email, id_address, name, password)
VALUES ('99999999999', now(), 'client@example.com', 1, 'admin', 'c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec');

INSERT INTO tb_product (id, name, price, quantity, id_category)
VALUES (1, 'Suco', 19.99, 10, 1),
(2, 'Sorvete', 29.99, 5, 2),
(3, 'Hamburguer', 39.99, 15, 3),
(4, 'Batata Frita', 9.99, 15, 4);