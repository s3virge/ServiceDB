SELECT * FROM servicedb.user_group;

INSERT INTO servicedb.user_group (`id`,`name`) VALUES (1, 'administrator');
INSERT INTO servicedb.user_group (`id`,`name`) VALUES (2, 'manager');
INSERT INTO servicedb.user_group (`id`,`name`) VALUES (3, 'user');

insert into servicedb.user ('login','password') values ('admin', 'admin');

DELETE FROM `servicedb`.`user_groups` WHERE `name`='manager' limit 6;

DELETE FROM `servicedb`.`user_groups`;

UPDATE `servicedb`.`user_groups` SET `id`='2' WHERE `id`='13';


SET GLOBAL time_zone = '+2:00';
