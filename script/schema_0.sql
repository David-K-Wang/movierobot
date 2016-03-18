DROP TABLE IF EXISTS `movie_ticket_info`;
CREATE TABLE `movie_ticket_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source` varchar(1024) NOT NULL DEFAULT '',
  `movie_name` varchar(1024) NOT NULL DEFAULT '',
  `cinema_name` varchar(1024) NOT NULL DEFAULT '',
  `city_name` varchar(64) NOT NULL DEFAULT '',
  `biz_district_name` varchar(1024) NOT NULL DEFAULT '',
  `current_price` int(11) DEFAULT 0,
  `original_price` int(11) DEFAULT 0,
  `movie_pic_url` varchar(1024) NOT NULL DEFAULT '',
  `url` varchar(1024) NOT NULL DEFAULT '',
  `movie_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
