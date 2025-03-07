/*
 Navicat Premium Data Transfer

 Source Schema         : superpoint

 Server Type    : MySQL
 Server Version : 50744 (5.7.44-log)

 Date: 02/03/2024 22:40:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table 场地设施表
-- ----------------------------
DROP TABLE IF EXISTS `facility`;
CREATE TABLE `facility`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `name`     varchar(255) NOT NULL,
    `province` varchar(255) DEFAULT NULL,
    `city`     varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table 请求日志表
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`
(
    `id`                    bigint(20)                          NOT NULL AUTO_INCREMENT,
    `user`                  varchar(255)                        NOT NULL,
    `platform`              enum ('微信公众号-阿左小院','未知') NOT NULL DEFAULT '未知',
    `receive_message_json`  varchar(255)                        NOT NULL,
    `response_message_json` varchar(255)                        NOT NULL,
    `create_time`           datetime                                     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table 比赛表
-- ----------------------------
DROP TABLE IF EXISTS `match`;
CREATE TABLE `match`
(
    `id`               int(11) NOT NULL AUTO_INCREMENT,
    `name`             varchar(255) DEFAULT NULL,
    `apply_start_time` datetime     DEFAULT NULL,
    `apply_end_time`   datetime     DEFAULT NULL,
    `match_start_time` datetime     DEFAULT NULL,
    `match_end_time`   datetime     DEFAULT NULL,
    `series_name`      varchar(255) DEFAULT NULL,
    `create_time`      datetime     DEFAULT CURRENT_TIMESTAMP,
    `update_time`      datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table 比赛x团队关联表
-- ----------------------------
DROP TABLE IF EXISTS `match_team_relation`;
CREATE TABLE `match_team_relation`
(
    `match_id` int(11) NOT NULL,
    `team_id`  int(11) NOT NULL,
    UNIQUE KEY `uniq_match_team_id` (`match_id`, `team_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table 运动员表（用户表）
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player`
(
    `id`             int(11)      NOT NULL AUTO_INCREMENT,
    `nick_name`      varchar(32)  NOT NULL,
    `wechat_user`    varchar(255) NOT NULL            DEFAULT 'no_user',
    `avatar` varchar(255) DEFAULT '',
    `gender`         enum ('male','female','unknown') DEFAULT 'unknown',
    `phone_number`   varchar(255)                     DEFAULT NULL,
    `id_card_number` varchar(255)                     DEFAULT NULL,
    `create_time`    datetime                         DEFAULT CURRENT_TIMESTAMP,
    `update_time`    datetime                         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_wechat` (`wechat_user`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table 运动员x团队关联表
-- ----------------------------
DROP TABLE IF EXISTS `player_team_relation`;
CREATE TABLE `player_team_relation`
(
    `player_id`   int(11) NOT NULL,
    `team_id`     int(11) NOT NULL,
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uniq_player_team_id` (`player_id`, `team_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table 点位表（需要打卡的点位）
-- ----------------------------
DROP TABLE IF EXISTS `point`;
CREATE TABLE `point`
(
    `id`          int(11)                          NOT NULL AUTO_INCREMENT,
    `name`        varchar(16)                      NOT NULL,
    `facility_id` int(11)                          NOT NULL,
    `match_id`    int(11)                          NOT NULL,
    `point_type`  enum ('necessary','unnecessary') NOT NULL,
    `lane`        varchar(32)                      NOT NULL DEFAULT '',
    `geo_point`   point                                     DEFAULT NULL,
    `create_time` datetime                                  DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_facility` (`facility_id`),
    KEY `idx_match` (`match_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table 运动员打卡记录表
-- ----------------------------
DROP TABLE IF EXISTS `punch_record`;
CREATE TABLE `punch_record`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `player_id`     int(11) unsigned    NOT NULL,
    `point_id`      int(11) unsigned    NOT NULL,
    `match_id`      int(11) unsigned    NOT NULL,
    `team_id`       int(11) unsigned    NOT NULL,
    `facility_id`   int(11) unsigned    NOT NULL,
    `expect_serial` int(11)               DEFAULT '-1',
    `punched`       enum ('true','false') DEFAULT 'false',
    `create_time`   datetime              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `punch_time`    datetime              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_player_point_match_id` (`player_id`, `match_id`, `point_id`),
    KEY `idx_team_id` (`team_id`),
    KEY `idx_match_id` (`match_id`),
    KEY `idx_facility_id` (`facility_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table 团队表
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team`
(
    `id`          int(11)     NOT NULL AUTO_INCREMENT,
    `name`        varchar(64) NOT NULL,
    `active`      enum ('true','false') DEFAULT 'true',
    `create_time` datetime              DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
