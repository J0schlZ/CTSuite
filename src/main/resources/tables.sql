CREATE TABLE IF NOT EXISTS `ctsuite_homes` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `owner` varchar(36) NOT NULL,
  `server` varchar(100) NOT NULL,
  `world` varchar(100) NOT NULL,
  `x` double DEFAULT NULL,
  `y` double DEFAULT NULL,
  `z` double DEFAULT NULL,
  `yaw` float DEFAULT NULL,
  `pitch` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `owner` (`owner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ctsuite_mail` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sender` varchar(36) NOT NULL,
  `receiver` varchar(36) NOT NULL,
  `message` longtext NOT NULL,
  `seen` tinyint(1) NOT NULL DEFAULT '0',
  `timestamp` bigint(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `ctsuite_players` (
  `uuid` varchar(36) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `server` varchar(32) DEFAULT NULL,
  `world` varchar(32) DEFAULT NULL,
  `online` varchar(1) NOT NULL DEFAULT '0',
  `first_login` int(11) DEFAULT NULL,
  `last_login` int(11) DEFAULT NULL,
  `last_logout` int(11) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `ctsuite_server` (
  `name` varchar(32) NOT NULL,
  `online` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `ctsuite_spawns` (
  `type` int(10) UNSIGNED NOT NULL,
  `server` varchar(100) NOT NULL,
  `world` varchar(100) NOT NULL,
  `x` double DEFAULT NULL,
  `y` double DEFAULT NULL,
  `z` double DEFAULT NULL,
  `yaw` float DEFAULT NULL,
  `pitch` float DEFAULT NULL,
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ctsuite_warps` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `server` varchar(100) NOT NULL,
  `world` varchar(100) NOT NULL,
  `x` double DEFAULT NULL,
  `y` double DEFAULT NULL,
  `z` double DEFAULT NULL,
  `yaw` float DEFAULT NULL,
  `pitch` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ctsuite_worlds` (
  `name` varchar(32) NOT NULL,
  `server` varchar(32) DEFAULT NULL,
  `loaded` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`name`),
  KEY `server` (`server`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
COMMIT;