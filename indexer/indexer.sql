create database if not exists indexer_DB;

use indexer_DB;

drop table if exists indexer;

CREATE TABLE `indexer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word` varchar(64) not NULL,
  `url` varchar(64) not NULL,
  `header` boolean not NULL,
  `freq` int(11) not NULL,
  `date_of_creation` dateTime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
