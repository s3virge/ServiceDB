SELECT * FROM servicedb.user_groups;

INSERT INTO servicedb.user_groups (`id`,`name`) VALUES (1, 'administrator');
INSERT INTO servicedb.user_groups (`id`,`name`) VALUES (2, 'manager');
INSERT INTO servicedb.user_groups (`id`,`name`) VALUES (3, 'user');

DELETE FROM `servicedb`.`user_groups` WHERE `name`='manager' limit 6;

DELETE FROM `servicedb`.`user_groups`;

UPDATE `servicedb`.`user_groups` SET `id`='2' WHERE `id`='13';
