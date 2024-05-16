CREATE DATABASE  IF NOT EXISTS `ht_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ht_db`;

-- Host: localhost    Database: ht_db

--
-- Table structure for table `ItemProduct`
--

DROP TABLE IF EXISTS `ItemProduct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ItemProduct` (
  `ItemProductId` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(300) NOT NULL,
  `LabelName` varchar(20) NOT NULL,
  `Price` decimal(5,2) NOT NULL,
  PRIMARY KEY (`ItemProductId`),
  UNIQUE KEY `LabelName_UNIQUE` (`LabelName`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ItemProduct`
--

LOCK TABLES `ItemProduct` WRITE;
/*!40000 ALTER TABLE `ItemProduct` DISABLE KEYS */;
INSERT INTO `ItemProduct` VALUES (1,'(Cold) Signature Brown Sugar Pearl Milk Tea','(Cold) Sig BrSg Prl',6.50),(2,'(Hot) Signature Brown Sugar Pearl Milk Tea ','(Hot) Sig BrSg Prl',7.45),(3,'(Cold) Original Pearl Milk Tea	','(Cold) Pearl Mlk',6.50),(4,'(Hot) Original Pearl Milk Tea	','(Hot) Pearl Mlk',7.45),(5,'(Cold) Black Diamond Milk Tea','(Cold) Blk Dmd Mlk',7.50),(6,'(Cold) Red Bean Pearl Milk Tea','(Hot) Blk Dmd Mlk',7.45),(7,'(Hot) Red Bean Pearl Milk Tea','(Hot) Red Bn Prl Mlk',8.35),(8,'(Cold) Earl Grey Milk Tea','(Cold) Erl Gry Mlk',6.50),(9,'(Hot) Earl Grey Milk Tea','(Hot) Erl Gry Mlk',7.45),(10,'(Cold) Signature Milk Tea','(Cold) Sig Mlk',5.55),(11,'(Hot) Signature Milk Tea','(Hot) Sig Mlk',6.50),(12,'(Cold) Original Milk Tea','(Cold) Org Mlk',5.55),(13,'(Hot) Original Milk Tea','(Hot) Org Mlk',6.50),(14,'(Cold) Signature Coffee','(Cold) Sig Coff',8.35),(15,'(Hot) Signature Coffee ','(Hot) Sig Coff',8.35),(16,'(Cold) Coco Mocha','(Cold) Coco Mocha',8.35),(17,'(Hot) Coco Mocha','(Hot) Coco Mocha',8.35),(21,'(Cold) Hazelnut Latte','(Cold) Hznut Latte',8.35),(22,'(Hot) Hazelnut Latte','(Hot) Hznut Latte',8.35),(23,'(Cold) Americano ','(Cold) Americano',7.45),(24,'(Hot) Americano','(Hot) Americano',7.45);
/*!40000 ALTER TABLE `ItemProduct` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Order`
--

DROP TABLE IF EXISTS `Order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Order` (
  `OrderId` int NOT NULL AUTO_INCREMENT,
  `OrderNumber` int NOT NULL,
  `TransactionDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `GrandTotal` decimal(7,2) NOT NULL,
  `TenderedCash` decimal(7,2) NOT NULL,
  `Change` decimal(7,2) NOT NULL,
  `TotalOrderItem` int NOT NULL,
  `SubTotal` decimal(7,2) NOT NULL,
  `Rounding` decimal(5,2) NOT NULL,
  `ServiceTax` decimal(7,2) NOT NULL,
  PRIMARY KEY (`OrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Order`
--

LOCK TABLES `Order` WRITE;
/*!40000 ALTER TABLE `Order` DISABLE KEYS */;
/*!40000 ALTER TABLE `Order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OrderItem`
--

DROP TABLE IF EXISTS `OrderItem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OrderItem` (
  `OrderItem` int NOT NULL AUTO_INCREMENT,
  `ItemProduct` int NOT NULL,
  `Order` int NOT NULL,
  `Quantity` int NOT NULL,
  `SubTotalAmount` decimal(7,2) NOT NULL,
  `SequenceNumber` int NOT NULL,
  `OrderStatus` varchar(20) NOT NULL DEFAULT 'Processing',
  `ReadyTime` datetime DEFAULT NULL,
  PRIMARY KEY (`OrderItem`),
  KEY `OrderItem_ItemProduct_FK_idx` (`ItemProduct`),
  KEY `OrderItem_Order_FK_idx` (`Order`),
  CONSTRAINT `OrderItem_ItemProduct_FK` FOREIGN KEY (`ItemProduct`) REFERENCES `ItemProduct` (`ItemProductId`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `OrderItem_Order_FK` FOREIGN KEY (`Order`) REFERENCES `Order` (`OrderId`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OrderItem`
--

LOCK TABLES `OrderItem` WRITE;
/*!40000 ALTER TABLE `OrderItem` DISABLE KEYS */;
/*!40000 ALTER TABLE `OrderItem` ENABLE KEYS */;
UNLOCK TABLES;
