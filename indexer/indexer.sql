create database if not exists indexer_DB;

use indexer_DB;

drop table if exists indexer;
CREATE TABLE `indexer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word` LONGTEXT not NULL,
  `url` LONGTEXT not NULL,
  `header` boolean not NULL,
  `freq` double not NULL,
  `date_of_creation` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


drop table if exists RankTable;
CREATE TABLE `RankTable` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `URLfrom` LONGTEXT not NULL,
    `URLto` LONGTEXT not NULL,
    PRIMARY KEY (`id`)
)   ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

drop table if exists UrlData;
CREATE TABLE `UrlData` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `URLLink` LONGTEXT not NULL,
    `URLTitle` LONGTEXT not NULL,
    `URLContent` LONGTEXT not NULL,
    PRIMARY KEY (`id`)
)   ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

drop table if exists phrase_searching;
CREATE TABLE `phrase_searching` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` LONGTEXT not NULL,
  `document` LONGTEXT not NULL,
  `date_of_creation` date DEFAULT NULL,
  `rank`  double NOT NULL,
  `glance` LONGTEXT not NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



drop table if exists images_urls;
CREATE TABLE `images_urls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `page_url` LONGTEXT not NULL,
  `image_url` LONGTEXT not NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


drop table if exists user_preferables;
CREATE TABLE `user_preferables` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` LONGTEXT not NULL,
  `website` LONGTEXT not NULL,
  `freq` double not NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


drop table if exists TRENDS;
CREATE TABLE `TRENDS` (
  `Name` VARCHAR (100) not NULL,
  `Rank` int(11) not NULL,
  `Country` VARCHAR (100) not NULL,
   PRIMARY KEY (`Name`,`Country`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


ALTER DATABASE indexer_DB DEFAULT COLLATE utf8_unicode_ci;

ALTER TABLE indexer CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

ALTER TABLE phrase_searching CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

ALTER TABLE RankTable CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

ALTER TABLE images_urls CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

ALTER TABLE user_preferables CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

ALTER TABLE TRENDS CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

ALTER TABLE UrlData CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;



