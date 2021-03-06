DROP TABLE IF EXISTS `movie_ticket_info`;
CREATE TABLE `movie_ticket_info` (
  `id` varchar(1024) NOT NULL DEFAULT '',
  `source_type` varchar(64) NOT NULL DEFAULT '',
  `movie_name` varchar(128) NOT NULL DEFAULT '',
  `cinema_name` varchar(128) NOT NULL DEFAULT '',
  `city_name` varchar(64) NOT NULL DEFAULT '',
  `biz_district_name` varchar(128) NOT NULL DEFAULT '',
  `current_price` int(11) DEFAULT '0',
  `original_price` int(11) DEFAULT '0',
  `movie_pic_url` varchar(1024) NOT NULL DEFAULT '',
  `source_url` varchar(1024) NOT NULL DEFAULT '',
  `hall` varchar(128) NOT NULL DEFAULT '',
	`movie_desc` varchar(1024) NOT NULL DEFAULT '',
  `movie_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;