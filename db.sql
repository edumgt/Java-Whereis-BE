-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.9.5-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- homedb 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `homedb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `homedb`;

-- 테이블 homedb.board 구조 내보내기
CREATE TABLE IF NOT EXISTS `board` (
  `article_no` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL DEFAULT '0',
  `subject` varchar(50) NOT NULL DEFAULT '0',
  `content` varchar(50) NOT NULL DEFAULT '0',
  `hit` tinyint(4) NOT NULL DEFAULT 0,
  `register_time` varchar(50) NOT NULL DEFAULT '0',
  `bullet` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`article_no`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 homedb.board:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
INSERT INTO `board` (`article_no`, `user_id`, `subject`, `content`, `hit`, `register_time`, `bullet`) VALUES
	(1, 'test0001', '테스트', '<p>테스트 입니다.</p>', 1, '2024-02-17 16:06:28', '일반');
/*!40000 ALTER TABLE `board` ENABLE KEYS */;

-- 테이블 homedb.dongcode 구조 내보내기
CREATE TABLE IF NOT EXISTS `dongcode` (
  `dongname` varchar(50) DEFAULT NULL,
  `dongcode` varchar(50) DEFAULT NULL,
  `gugunname` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 homedb.dongcode:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `dongcode` DISABLE KEYS */;
/*!40000 ALTER TABLE `dongcode` ENABLE KEYS */;

-- 테이블 homedb.housedeal 구조 내보내기
CREATE TABLE IF NOT EXISTS `housedeal` (
  `hd_aptcode` varchar(50) DEFAULT NULL,
  `floor` varchar(50) DEFAULT NULL,
  `area` varchar(50) DEFAULT NULL,
  `dealamount` varchar(50) DEFAULT NULL,
  `dealyear` varchar(50) DEFAULT NULL,
  `dealmonth` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 homedb.housedeal:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `housedeal` DISABLE KEYS */;
/*!40000 ALTER TABLE `housedeal` ENABLE KEYS */;

-- 테이블 homedb.houseinfo 구조 내보내기
CREATE TABLE IF NOT EXISTS `houseinfo` (
  `hi_aptcode` varchar(50) DEFAULT NULL,
  `buildyear` varchar(50) DEFAULT NULL,
  `apartmentname` varchar(50) DEFAULT NULL,
  `dong` varchar(50) DEFAULT NULL,
  `jibun` varchar(50) DEFAULT NULL,
  `lng` varchar(50) DEFAULT NULL,
  `lat` varchar(50) DEFAULT NULL,
  `img` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 homedb.houseinfo:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `houseinfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `houseinfo` ENABLE KEYS */;

-- 테이블 homedb.myhouse 구조 내보내기
CREATE TABLE IF NOT EXISTS `myhouse` (
  `user_id` varchar(50) DEFAULT NULL,
  `aptcode` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 homedb.myhouse:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `myhouse` DISABLE KEYS */;
/*!40000 ALTER TABLE `myhouse` ENABLE KEYS */;

-- 테이블 homedb.user 구조 내보내기
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` varchar(50) NOT NULL,
  `user_password` varchar(50) NOT NULL,
  `email_id` varchar(50) NOT NULL,
  `email_domain` varchar(50) NOT NULL,
  `manager` varchar(50) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 homedb.user:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `user_password`, `email_id`, `email_domain`, `manager`, `user_name`) VALUES
	('test0001', '11111', 'aaaaa', '@naver.com', 'manager', NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
