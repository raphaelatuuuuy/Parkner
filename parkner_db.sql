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
-- Table structure for table `parking_store`
--

DROP TABLE IF EXISTS `parking_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking_store` (
  `id` int NOT NULL,
  `vailable` tinyint(1) NOT NULL,
  `gen` varchar(50) NOT NULL,
  `regis` varchar(10) NOT NULL,
  `reference_id` int DEFAULT NULL,
  `time_in` varchar(20) DEFAULT NULL,
  `date_in` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Sample data for parking_store
LOCK TABLES `parking_store` WRITE;
INSERT INTO `parking_store` VALUES
  (1,0,'Toyota','ABC-1234',12345678,'08:15 AM','2025-06-11'),
  (2,1,'','',NULL,NULL,NULL),
  (3,0,'Honda','XYZ-5678',87654321,'09:30 AM','2025-06-11'),
  (4,1,'','',NULL,NULL,NULL),
  (5,0,'Ford','DEF-4321',23456789,'10:45 AM','2025-06-11');
UNLOCK TABLES;

--
-- Dumping data for table `parking_store`
--

LOCK TABLES `parking_store` WRITE;
/*!40000 ALTER TABLE `parking_store` DISABLE KEYS */;
/*!40000 ALTER TABLE `parking_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `id` int NOT NULL,
  `gen` varchar(50) NOT NULL,
  `regis` varchar(10) NOT NULL,
  `totalPrice` int NOT NULL,
  `change` decimal(10,2) DEFAULT NULL,
  `reference_id` int DEFAULT NULL,
  `time_in` varchar(50) DEFAULT NULL,
  `time_out` varchar(50) DEFAULT NULL,
  `date_in` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Sample data for report
LOCK TABLES `report` WRITE;
INSERT INTO `report` VALUES
  (1,'Toyota','ABC-1234',100,0.00,12345678,'08:15 AM','10:15 AM','2025-06-11'),
  (2,'Honda','XYZ-5678',150,0.00,87654321,'09:30 AM','12:00 PM','2025-06-11'),
  (3,'Ford','DEF-4321',50,0.00,23456789,'10:45 AM','11:45 AM','2025-06-11');
UNLOCK TABLES;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
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
INSERT INTO `totalprice` VALUES (2,50);
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

-- Dump completed on 2025-06-11 18:50:40
