-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: testdb
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `nation_code`
--

DROP TABLE IF EXISTS `nation_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nation_code` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `lat` varchar(255) DEFAULT NULL,
  `lon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nation_code`
--

LOCK TABLES `nation_code` WRITE;
/*!40000 ALTER TABLE `nation_code` DISABLE KEYS */;
INSERT INTO `nation_code` VALUES (90,'Incheon','ICN','37.4692','126.4505'),(91,'Gimpo','GMP','37.5583','126.7917'),(92,'Gimhae','PUS','35.1794','128.9381'),(93,'Jeju','CJU','33.5104','126.4914'),(94,'Daegu','TAE','35.8981','128.6414'),(95,'Cheongju','CJJ','36.7161','127.4989'),(96,'Yangyang','YNY','38.0613','128.6692'),(97,'Gwangju','KWJ','35.1269','126.8089'),(98,'Ulsan','USN','35.5933','129.3514'),(99,'Wonju','WJU','37.4419','127.9608'),(100,'Narita','NRT','35.7721','140.3923'),(101,'Haneda','HND','35.5494','139.7798'),(102,'Kansai','KIX','34.4347','135.2442'),(103,'Chubu Centrair','NGO','34.8584','136.8053'),(104,'Fukuoka','FUK','33.5859','130.4501'),(105,'Sapporo New Chitose','CTS','42.7753','141.6932'),(106,'Naha','OKA','26.1958','127.6456'),(107,'Osaka','ITM','34.7855','135.438'),(108,'Hiroshima','HIJ','34.4364','132.9199'),(109,'Sendai','SDJ','38.1397','140.9175'),(110,'Kagoshima','KOJ','31.8014','130.7193'),(111,'Beijing Capital','PEK','40.0799','116.6031'),(112,'Shanghai Pudong','PVG','31.1443','121.8083'),(113,'Guangzhou Baiyun','CAN','23.3925','113.2991'),(114,'Shenzhen Bao\'an','SZX','22.6397','113.8105'),(115,'Chengdu Shuangliu','CTU','30.5783','103.9463'),(116,'Xi\'an Xianyang','XIY','34.4416','108.7514'),(117,'Hangzhou Xiaoshan','HGH','30.236','120.429'),(118,'Chongqing Jiangbei','CKG','29.7199','106.6419'),(119,'Qingdao Liuting','TAO','36.2661','120.3959'),(120,'Dalian Zhoushuizi','DLC','38.9657','121.5382'),(121,'Singapore Changi','SIN','1.3644','103.9915'),(122,'Bangkok Suvarnabhumi','BKK','13.6933','100.7501'),(123,'Kuala Lumpur International','KUL','2.7456','101.7099'),(124,'Jakarta Soekarno-Hatta','CGK','-6.1256','106.655'),(125,'Manila Ninoy Aquino','MNL','14.5123','121.017'),(126,'Ho Chi Minh Tan Son Nhat','SGN','10.8185','106.6511'),(127,'Phuket International','HKT','8.1132','98.316'),(128,'Denpasar Bali Ngurah Rai','DPS','-8.7482','115.1667'),(129,'Yangon International','RGN','16.9074','96.1332'),(130,'Hanoi Noi Bai','HAN','21.2212','105.8055'),(134,'Oita','OIT','33.479797404158745','131.73642610304157');
/*!40000 ALTER TABLE `nation_code` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-17  8:51:23
