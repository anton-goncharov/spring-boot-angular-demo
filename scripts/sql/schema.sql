CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `is_active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_user` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `meal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `calories` int(11) NOT NULL,
  `meal_date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `updated` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_meal` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  KEY `FK_user_role` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `user_settings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `calories_expected_daily` int(11) DEFAULT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_user_settings` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
