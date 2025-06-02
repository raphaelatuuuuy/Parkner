-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 20, 2021 at 11:34 PM
-- Server version: 8.0.18
-- PHP Version: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `parking`
--

-- --------------------------------------------------------

--
-- Table structure for table `parking_store`
--

CREATE TABLE `parking_store` (
  `id` int(5) NOT NULL,
  `vailable` tinyint(1) NOT NULL,
  `gen` varchar(50) NOT NULL,
  `regis` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `parking_store`
--

INSERT INTO `parking_store` (`id`, `vailable`, `gen`, `regis`) VALUES
(1, 1, '', ''),
(2, 1, '', ''),
(3, 1, '', ''),
(4, 1, '', ''),
(5, 1, '', ''),
(6, 1, '', ''),
(7, 1, '', '');

-- --------------------------------------------------------

--
-- Table structure for table `report`
--

CREATE TABLE `report` (
  `id` int(5) NOT NULL,
  `gen` varchar(50) NOT NULL,
  `regis` varchar(10) NOT NULL,
  `totalPrice` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `report`
--

INSERT INTO `report` (`id`, `gen`, `regis`, `totalPrice`) VALUES
(6, 'Toyota', '1กข', 200),
(6, 'Toyota', '1กข', 300),
(6, 'Toyota', '1กข', 350),
(6, 'Toyota', '1กข', 200),
(0, 'null', 'null', 150),
(0, 'null', 'null', 250),
(3, 'Yamaha', '4พฟ', 250),
(4, 'Yamaha', '4พฟ', 200),
(6, 'Toyota', '1กข', 200),
(7, 'Toyota', '1กข', 500),
(4, 'Yamaha', '4พฟ', 200),
(5, 'Toyota', '1บน', 250),
(4, 'Toyota', '1กค', 400),
(4, 'Toyota', '1กค', 750);

-- --------------------------------------------------------

--
-- Table structure for table `totalprice`
--

CREATE TABLE `totalprice` (
  `id` int(5) NOT NULL,
  `price` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `totalprice`
--

INSERT INTO `totalprice` (`id`, `price`) VALUES
(4, 750);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
