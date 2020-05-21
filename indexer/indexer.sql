create database if not exists indexer_DB;

use indexer_DB;

drop table if exists indexer;
CREATE TABLE `indexer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word` varchar(64) not NULL,
  `url` varchar(64) not NULL,
  `header` boolean not NULL,
  `freq` double not NULL,
  `date_of_creation` dateTime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


drop table if exists RankTable;
CREATE TABLE `RankTable` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `URLfrom` varchar(64) not NULL,
    `URLto` varchar(64) not NULL,
    PRIMARY KEY (`id`)
)   ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



drop table if exists phrase_searching;
CREATE TABLE `phrase_searching` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(64) not NULL,
  `document` LONGTEXT not NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



drop table if exists images_urls;
CREATE TABLE `images_urls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `page_url` varchar(64) not NULL,
  `image_url` varchar(64) not NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


