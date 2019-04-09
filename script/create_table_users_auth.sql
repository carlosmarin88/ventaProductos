CREATE TABLE `db_springboot`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `enabled` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC));


CREATE TABLE `db_springboot`.`authorities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `authority` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_id_authority_unique` (`user_id` ASC, `authority` ASC),
  CONSTRAINT `fk_authorities_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `db_springboot`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

/*password de los dos es 12345*/
insert into users (username,password,enabled) value ('carlos', '$2a$10$hOclaNBMHQTGi2t7WTF/nex1/826pIYhCCsO8lEOO9309xxbonamu', 1);
insert into users (username,password,enabled) value ('admin', '$2a$10$eClCHN4b/RyW2DyxP97sJuK68Bv0AexPiwbel/w99yWwEvu0u67ae', 1);

insert into authorities  (user_id,authority) values (1,'ROLE_USER');
insert into authorities  (user_id,authority) values (2,'ROLE_USER');
insert into authorities  (user_id,authority) values (2,'ROLE_ADMIN');