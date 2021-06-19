
create table user(id int primary key auto_increment,
                    name varchar(128) not null,
                    password varchar(24),
                    city_id int,
                    description varchar(256)
);

INSERT INTO `user` VALUES (1, 'zhangsan', '123456', 1, 'hello, i\'m zhangsan');
INSERT INTO `user` VALUES (2, 'lisi', '123456', 2, 'hi, i\'m lisi');
INSERT INTO `user` VALUES (3, 'wangermazi', '123456', 2, 'kekeke, i\'m wangermazi');
INSERT INTO `user` VALUES (4, 'elon musk', '123456', 100, 'i\'m elon, the dogelon\'s elon. not musk');
INSERT INTO `user` VALUES (7, 'hello', '123456', NULL, 'world');
INSERT INTO `user` VALUES (100, 'name', '123456', NULL, 'name && pawd are successfully.');

