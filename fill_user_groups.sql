SELECT * FROM servicedb.user_group;

SELECT * FROM servicedb.user;

INSERT INTO servicedb.user_group (`id`,`name`) VALUES (1, 'administrator');
INSERT INTO servicedb.user_group (`id`,`name`) VALUES (2, 'manager');
INSERT INTO servicedb.user_group (`id`,`name`) VALUES (3, 'user');

DELETE FROM `servicedb`.`user_groups` WHERE `name`='manager' limit 6;

DELETE FROM `servicedb`.`user_groups`;

UPDATE `servicedb`.`user_groups` SET `id`='2' WHERE `id`='13';

SET GLOBAL time_zone = '+2:00';
