CREATE DATABASE  IF NOT EXISTS `parking` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `parking`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: parking
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `parking_slot`
--

DROP TABLE IF EXISTS `parking_slot`;
CREATE TABLE `parking_slot` (
  `id` INT NOT NULL PRIMARY KEY DEFAULT 1,
  `total_slots` INT NOT NULL,
  `maintenance_reserved` INT NOT NULL,
  `occupied` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Only one row should exist:
-- Set occupied to 3, matching the 3 initial records in parking_store
INSERT INTO `parking_slot` (`id`, `total_slots`, `maintenance_reserved`, `occupied`) VALUES (1, 50, 0, 3);

--
-- Table structure for table `parking_store`
--

DROP TABLE IF EXISTS `parking_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking_store` (
  `gen` varchar(50) NOT NULL,
  `regis` varchar(10) NOT NULL,
  `reference_id` int DEFAULT NULL,
  `time_in` varchar(20) DEFAULT NULL,
  `date_in` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_store`
--

LOCK TABLES `parking_store` WRITE;
/*!40000 ALTER TABLE `parking_store` DISABLE KEYS */;
INSERT INTO `parking_store` (`gen`, `regis`, `reference_id`, `time_in`, `date_in`) VALUES
('Toyota', 'ABC123', 10000001, '08:00 AM', '2025-06-13'),
('Honda', 'XYZ789', 10000002, '09:30 AM', '2025-06-13'),
('Nissan', 'DEF456', 10000003, '10:15 AM', '2025-06-13');
/*!40000 ALTER TABLE `parking_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `gen` varchar(50) NOT NULL,
  `regis` varchar(10) NOT NULL,
  `totalPrice` int NOT NULL,
  `change` decimal(10,2) DEFAULT NULL,
  `reference_id` int DEFAULT NULL,
  `time_in` varchar(50) DEFAULT NULL,
  `time_out` varchar(50) DEFAULT NULL,
  `date_in` text,
  `qr_info` text,
  `payment_method` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
INSERT INTO `report` (`id`, `gen`, `regis`, `totalPrice`, `change`, `reference_id`, `time_in`, `time_out`, `date_in`, `qr_info`, `payment_method`) VALUES
(1, 'Toyota', 'ABC123', 100, 0.00, 10000001, '08:00 AM', '10:00 AM', '2025-06-13', 'PARKNER PAY\nReference: 10000001\nCar: Toyota\nPlate: ABC123\nAmount: 100 pesos', 'Cash'),
(2, 'Honda', 'XYZ789', 50, 0.00, 10000002, '09:30 AM', '10:30 AM', '2025-06-13', 'PARKNER PAY\nReference: 10000002\nCar: Honda\nPlate: XYZ789\nAmount: 50 pesos', 'Digital');
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `totalprice`
--

DROP TABLE IF EXISTS `totalprice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `totalprice` (
  `id` int NOT NULL,
  `price` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `totalprice`
--

LOCK TABLES `totalprice` WRITE;
/*!40000 ALTER TABLE `totalprice` DISABLE KEYS */;
INSERT INTO `totalprice` VALUES (1,50);
/*!40000 ALTER TABLE `totalprice` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-13 17:31:48
