/*Populate tables*/
insert into clientes(id,nombre,apellido,email,create_at, foto) values(1,'Carlos', 'Marin', 'carlos@mail.com', '2017-08-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(2,'Hernan', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(3,'Gabriel', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(4,'Pocho', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(5,'Nicolas', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(6,'David', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(7,'Damian', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(8,'Esteban', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(9,'Pablo', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(10,'Ricardo', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(11,'Marcos', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(12,'Fabian', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(13,'Oscar', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(14,'Cristian', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(15,'Ezequiel', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(16,'Emanuel', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(17,'Gabriel', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(18,'Bruno', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(19,'Jorge', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(20,'Julian', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(21,'Adrian', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(22,'Elias', 'Gomez', 'josess@mail.com', '2018-03-28','');
insert into clientes(id,nombre,apellido,email,create_at, foto) values(23,'Moises', 'Gomez', 'josess@mail.com', '2018-03-28','');

/* populate tabla producto */
insert into productos(nombre,precio,create_at) values ('Panasonic Pantalla LCD', 259990, NOW());
insert into productos(nombre,precio,create_at) values ('Sony Camara Digital DSC-W3208', 123490, NOW());
insert into productos(nombre,precio,create_at) values ('Apple Ipod Suffle', 149990, NOW());
insert into productos(nombre,precio,create_at) values ('Sony Notebook Z110', 37990, NOW());
insert into productos(nombre,precio,create_at) values ('Hewlett Packard Multifuncional F2280', 69990, NOW());
insert into productos(nombre,precio,create_at) values ('Bianchi Bicicleta Aro 26', 69990, NOW());
insert into productos(nombre,precio,create_at) values ('Mica Comoda 5 cajones', 29990, NOW());

/* creamos algunas facturas */
insert into facturas (descripcion, observacion, id_cliente, fecha) values('Factura equipos de oficina', null, 1, NOW());
insert into facturas_items (cantidad, id_factura, id_producto) values(1,1,1);
insert into facturas_items (cantidad, id_factura, id_producto) values(2,1,4);
insert into facturas_items (cantidad, id_factura, id_producto) values(1,1,5);
insert into facturas_items (cantidad, id_factura, id_producto) values(1,1,7);

insert into facturas (descripcion, observacion, id_cliente, fecha) values('Factura Bicicleta', 'Alguna nota importante!', 1, NOW());
insert into facturas_items (cantidad, id_factura, id_producto) values(3,2,6);


/* creacion de usuarios con roles*/
insert into users (username,password,enabled) value ('carlos', '$2a$10$hOclaNBMHQTGi2t7WTF/nex1/826pIYhCCsO8lEOO9309xxbonamu', 1);
insert into users (username,password,enabled) value ('admin', '$2a$10$eClCHN4b/RyW2DyxP97sJuK68Bv0AexPiwbel/w99yWwEvu0u67ae', 1);

insert into authorities  (user_id,authority) values (1,'ROLE_USER');
insert into authorities  (user_id,authority) values (2,'ROLE_USER');
insert into authorities  (user_id,authority) values (2,'ROLE_ADMIN');

