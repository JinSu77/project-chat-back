/* CREATE DATABASE IF NOT EXISTS `springbootdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE `springbootdb`;

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `firstName` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255), 
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

INSERT INTO `users` (`username`, `lastName`, `firstName`, `password`, `email`) VALUES
('administator', 'Rocher', 'Pierre', 'password', 'p.rocher@gmail.com');
 */