/*
 Navicat Premium Data Transfer

 Source Server         : mysql8-test
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:33068
 Source Schema         : dinner

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 11/11/2022 16:38:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
BEGIN;
INSERT INTO `tb_user` VALUES ('ag', '123', '123@123.com');
INSERT INTO `tb_user` VALUES ('ag1', '123', '234@11.com');
INSERT INTO `tb_user` VALUES ('chenyinting', '666666', '123456@123.com');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
