CREATE TABLE `invoice` (
  `INVOICE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `INVOICE_NUM` int(11) DEFAULT NULL,
  `ORDER_ID` int(11) DEFAULT NULL,
  `INVOICE_DETAILS` varchar(250) DEFAULT NULL,
  `INVOICE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `STATUS` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`INVOICE_ID`),
  KEY `FK_INVOICE_ORDER` (`ORDER_ID`),
  CONSTRAINT `FK_INVOICE_ORDER` FOREIGN KEY (`ORDER_ID`) REFERENCES `orders` (`order_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE `account` (
  `account_id` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOMER_ID` int(11) NOT NULL,
  `OPEN_DATE` int(11) DEFAULT NULL,
  `NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`account_id`),
  KEY `FK_ACC_CUST` (`CUSTOMER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `customer` (
  `CUSTOMER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CREATE_TS` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FIRST_NAME` varchar(50) DEFAULT NULL,
  `MIDDLE_NAME` varchar(50) DEFAULT NULL,
  `LAST_NAME` varchar(50) DEFAULT NULL,
  `GIVEN_NAME` varchar(150) DEFAULT NULL,
  `GENDER` varchar(1) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `ADDRESS_LINE_1` varchar(250) DEFAULT NULL,
  `ADDRESS_LINE_2` varchar(250) DEFAULT NULL,
  `CITY` varchar(50) DEFAULT NULL,
  `STATE_PROVINCE` varchar(50) DEFAULT NULL,
  `MOBILE_NUM` int(15) DEFAULT NULL,
  `COMPANY_NAME` varchar(100) DEFAULT NULL,
  `ZIP_CODE` varchar(20) DEFAULT NULL,
  `COUNTRY` varchar(20) DEFAULT NULL,
  `OFFICE_PHONE_NUM` int(11) DEFAULT NULL,
  `NOTES` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`CUSTOMER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

CREATE TABLE `invoice_items` (
  `INVOICE_ITEM_ID` int(11) NOT NULL AUTO_INCREMENT,
  `QUANTITY` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `UNIT_PRICE` double DEFAULT NULL,
  `DISCOUNT` double DEFAULT NULL,
  `AMOUNT` double DEFAULT NULL,
  `INVOICE_ID` int(11) DEFAULT NULL,
  `ITEM_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`INVOICE_ITEM_ID`),
  KEY `fk_invoice_item_invoice` (`INVOICE_ID`),
  KEY `fk_invoice_item_item` (`ITEM_ID`),
  CONSTRAINT `fk_invoice_item_invoice` FOREIGN KEY (`INVOICE_ID`) REFERENCES `simple_invoice` (`INVOICE_ID`),
  CONSTRAINT `fk_invoice_item_item` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ITEM_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

CREATE TABLE `item` (
  `ITEM_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ITEM_TYPE_ID` int(11) DEFAULT NULL,
  `ITEM_CODE` varchar(20) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `UNIT_COST` double DEFAULT NULL,
  `CREATE_TS` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATE_TS` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `CREATED_BY` varchar(20) DEFAULT NULL,
  `UPDATED_BY` varchar(20) DEFAULT NULL,
  `ITEM_CATEGORY` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ITEM_ID`),
  KEY `FK_ITEM_ITEMTYPE` (`ITEM_TYPE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

CREATE TABLE `item_hist` (
  `ITEM_HIST_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ITEM_ID` int(11) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `UNIT_COST` double DEFAULT NULL,
  `CREATE_TS` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CREATED_BY` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ITEM_HIST_ID`),
  KEY `FK_ITEMHIST_ITEM` (`ITEM_ID`),
  CONSTRAINT `FK_ITEMHIST_ITEM` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `item_type` (
  `ITEM_TYPE_ID` int(11) NOT NULL,
  `CODE` varchar(25) DEFAULT NULL,
  `NAME` varchar(200) DEFAULT NULL,
  `PARENT_ITEM_TYPE_ID` int(11) DEFAULT NULL,
  `ITEM_CATEGORY` varchar(20) DEFAULT NULL,
  `CREATE_TS` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CREATED_BY` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ITEM_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `order_item` (
  `order_item_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORDER_ID` int(11) DEFAULT NULL,
  `ITEM_ID` int(11) DEFAULT NULL,
  `ITEM_QUANTITY` int(11) DEFAULT NULL,
  `ORDER_ITEM_DETAILS` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`order_item_ID`),
  KEY `FK_ORDERITEM_ORDER` (`ORDER_ID`),
  KEY `FK_ORDERITEM_ITEM` (`ITEM_ID`),
  CONSTRAINT `FK_ORDERITEM_ITEM` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ITEM_ID`),
  CONSTRAINT `FK_ORDERITEM_ORDER` FOREIGN KEY (`ORDER_ID`) REFERENCES `orders` (`ORDER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `orders` (
  `order_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOMER_ID` int(11) DEFAULT NULL,
  `ORDER_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ORDER_DETAILS` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`order_ID`),
  KEY `FK_ORDER_CUSTOMER` (`CUSTOMER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `simple_invoice` (
  `INVOICE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `INVOICE_NUM` int(11) DEFAULT NULL,
  `INVOICE_DETAILS` varchar(500) DEFAULT NULL,
  `INVOICE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `DISCOUNT` decimal(6,0) DEFAULT NULL,
  `AMOUNT` decimal(6,0) DEFAULT NULL,
  `CUSTOMER_ID` int(11) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`INVOICE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

CREATE TABLE `transaction` (
  `trans_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACCOUNT_ID` int(11) DEFAULT NULL,
  `INVOICE_ID` int(11) DEFAULT NULL,
  `TRAN_TYPE_ID` int(11) DEFAULT NULL,
  `TRAN_TS` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `AMOUNT` decimal(6,0) DEFAULT NULL,
  `COMMENTS` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`trans_ID`),
  KEY `FK_TRAN_TRANTYPE` (`TRAN_TYPE_ID`),
  KEY `FK_TRAN_ACCOUNT` (`ACCOUNT_ID`),
  KEY `FK_TRAN_INVOICE` (`INVOICE_ID`),
  CONSTRAINT `FK_TRAN_ACCOUNT` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `account` (`ACCOUNT_ID`),
  CONSTRAINT `FK_TRAN_INVOICE` FOREIGN KEY (`INVOICE_ID`) REFERENCES `invoice` (`INVOICE_ID`),
  CONSTRAINT `FK_TRAN_TRANTYPE` FOREIGN KEY (`TRAN_TYPE_ID`) REFERENCES `transaction_type` (`TRAN_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `transaction_type` (
  `TRAN_TYPE_ID` int(11) NOT NULL,
  `CODE` varchar(25) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`TRAN_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `user_id` varchar(20) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `prefix` varchar(20) DEFAULT NULL,
  `suffix` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_group` (
  `group_id` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_group_map` (
  `user_id` varchar(20) NOT NULL DEFAULT '',
  `group_id` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`user_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_login` (
  `user_id` varchar(20) NOT NULL,
  `password` varchar(500) DEFAULT NULL,
  `create_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_ts` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_uuid_map` (
  `user_id` int(11) NOT NULL,
  `uuid` varchar(50) NOT NULL,
  `create_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `customer_vehicle` (
  `cust_vehicle_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT NULL,
  `make` varchar(50) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `vin` varchar(50) DEFAULT NULL,
  `create_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_ts` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `created_by` varchar(20) DEFAULT NULL,
  `updated_by` varchar(20) DEFAULT NULL,
  `year` varchar(4) DEFAULT NULL,
  `mileage` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`cust_vehicle_id`),
  KEY `fk_cust_vehicle` (`customer_id`),
  CONSTRAINT `fk_cust_vehicle` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`CUSTOMER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

