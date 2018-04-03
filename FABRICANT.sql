-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 01, 2018 at 01:09 
-- Server version: 5.6.14
-- PHP Version: 5.5.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `LEGEND_OF_RETRO`
--

-- --------------------------------------------------------

--
-- Table structure for table `FABRICANT`
--

CREATE TABLE IF NOT EXISTS `FABRICANT` (
  `id_fabricant` int(11) NOT NULL AUTO_INCREMENT,
  `nom_fabricant` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id_fabricant`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `FABRICANT`
--

INSERT INTO `FABRICANT` (`id_fabricant`, `nom_fabricant`) VALUES
(1, 'Microids'),
(2, 'MavenHut'),
(3, 'Gameloft'),
(4, 'MavenHut'),
(5, 'Nintendo'),
(6, 'MavenHut'),
(7, 'Nintendo'),
(8, 'Microids');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
