CREATE TABLE IF NOT EXISTS `member`
(
    `id`                      BIGINT      NOT NULL AUTO_INCREMENT,
    `email`                   VARCHAR(50) NOT NULL,
    `name`                    VARCHAR(50) NOT NULL,
    `profile_url`             VARCHAR(2048) DEFAULT NULL,
    `major`                   VARCHAR(30)   DEFAULT NULL,
    `class_number`            INTEGER(9)    DEFAULT NULL,
    `provider`                VARCHAR(30) NOT NULL,
    `created_date_time`       DATETIME(6)   DEFAULT NULL,
    `last_modified_date_time` DATETIME(6)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_member_1` (`email`)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `administrator`
(
    `id`                      BIGINT      NOT NULL AUTO_INCREMENT,
    `email`                   VARCHAR(50) NOT NULL,
    `name`                    VARCHAR(50) NOT NULL,
    `created_date_time`       DATETIME(6) DEFAULT NULL,
    `last_modified_date_time` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_administrator_1` (`email`)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `organization`
(
    `id`                      BIGINT      NOT NULL AUTO_INCREMENT,
    `sub_domain`              VARCHAR(50) NOT NULL,
    `name`                    VARCHAR(50) NOT NULL,
    `category`                VARCHAR(30) NOT NULL,
    `profile_url`             VARCHAR(2048)        DEFAULT NULL,
    `description`             VARCHAR(255)         DEFAULT NULL,
    `members_count`           INTEGER     NOT NULL DEFAULT 0,
    `followers_count`         INTEGER     NOT NULL DEFAULT 0,
    `created_date_time`       DATETIME(6)          DEFAULT NULL,
    `last_modified_date_time` DATETIME(6)          DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_organization_1` (`sub_domain`),
    KEY `idx_organization_1` (`category`),
    KEY `idx_organization_2` (`followers_count`)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `organization_member_mapper`
(
    `id`                      BIGINT      NOT NULL AUTO_INCREMENT,
    `organization_id`         BIGINT      NOT NULL,
    `member_id`               BIGINT      NOT NULL,
    `role`                    VARCHAR(30) NOT NULL,
    `created_date_time`       DATETIME(6) DEFAULT NULL,
    `last_modified_date_time` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`),
    KEY `idx_organization_member_mapper_1` (`member_id`)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `organization_follower`
(
    `id`                      BIGINT NOT NULL AUTO_INCREMENT,
    `organization_id`         BIGINT NOT NULL,
    `member_id`               BIGINT NOT NULL,
    `created_date_time`       DATETIME(6) DEFAULT NULL,
    `last_modified_date_time` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `organization_board`
(
    `id`                      BIGINT       NOT NULL AUTO_INCREMENT,
    `sub_domain`              varchar(50)  NOT NULL,
    `member_id`               BIGINT       NOT NULL,
    `category`                    VARCHAR(30)  NOT NULL,
    `title`                   VARCHAR(200) NOT NULL,
    `content`                 VARCHAR(2048)         DEFAULT NULL,
    `start_date_time`         DATETIME(6)  NOT NULL,
    `end_date_time`           DATETIME(6)  NOT NULL,
    `image_url`               VARCHAR(2048)         DEFAULT NULL,
    `likes_count`             INTEGER      NOT NULL DEFAULT 0,
    `created_date_time`       DATETIME(6)           DEFAULT NULL,
    `last_modified_date_time` DATETIME(6)           DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_organization_board_1` (`sub_domain`),
    KEY `idx_organization_board_2` (`likes_count`),
    KEY `idx_organization_board_3` (`category`),
    KEY `idx_organization_board_4` (`start_date_time`, `end_date_time`)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `admin_board`
(
    `id`                      BIGINT       NOT NULL AUTO_INCREMENT,
    `administrator_id`        BIGINT       NOT NULL,
    `title`                   VARCHAR(200) NOT NULL,
    `content`                 VARCHAR(2048) DEFAULT NULL,
    `start_date_time`         DATETIME(6)  NOT NULL,
    `end_date_time`           DATETIME(6)  NOT NULL,
    `created_date_time`       DATETIME(6)   DEFAULT NULL,
    `last_modified_date_time` DATETIME(6)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `admin_board_1` (`start_date_time`, `end_date_time`)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `organization_board_like`
(
    `id`                      BIGINT NOT NULL AUTO_INCREMENT,
    `organization_board_id`   BIGINT NOT NULL,
    `member_id`               BIGINT NOT NULL,
    `created_date_time`       DATETIME(6) DEFAULT NULL,
    `last_modified_date_time` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`organization_board_id`) REFERENCES `organization_board` (`id`)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `delete_organization_board`
(
    `id`                        BIGINT       NOT NULL AUTO_INCREMENT,
    `sub_domain`                VARCHAR(50)  NOT NULL,
    `member_id`                 BIGINT       NOT NULL,
    `category`                      VARCHAR(30)  NOT NULL,
    `title`                     VARCHAR(200) NOT NULL,
    `content`                   VARCHAR(2048) DEFAULT NULL,
    `start_date_time`           DATETIME(6)  NOT NULL,
    `end_date_time`             DATETIME(6)  NOT NULL,
    `image_url`                 VARCHAR(2048) DEFAULT NULL,
    `back_up_id`                BIGINT       NOT NULL,
    `deleted_member_id`         BIGINT        DEFAULT NULL,
    `deleted_admin_member_id`   BIGINT        DEFAULT NULL,
    `back_up_created_date_time` DATETIME(6)  NOT NULL,
    `created_date_time`         DATETIME(6)   DEFAULT NULL,
    `last_modified_date_time`   DATETIME(6)   DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `board_comment`
(
    `id`                      BIGINT        NOT NULL AUTO_INCREMENT,
    `type`                    VARCHAR(30)   NOT NULL,
    `board_id`                BIGINT        NOT NULL,
    `member_id`               BIGINT        NOT NULL,
    `parent_id`               BIGINT                 DEFAULT NULL,
    `content`                 VARCHAR(2048) NOT NULL,
    `depth`                   INTEGER       NOT NULL DEFAULT 0,
    `is_deleted`              TINYINT(1)    NOT NULL DEFAULT FALSE,
    `created_date_time`       DATETIME(6)            DEFAULT NULL,
    `last_modified_date_time` DATETIME(6)            DEFAULT NULL,
    `comment_like_counts`     INT           NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_board_comment_1` (`type`, `board_id`)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `board_comment_like`
(
    `id`                      BIGINT NOT NULL AUTO_INCREMENT,
    `board_comment_id`        BIGINT NOT NULL,
    `member_id`               BIGINT NOT NULL,
    `created_date_time`       DATETIME(6) DEFAULT NULL,
    `last_modified_date_time` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`board_comment_id`) REFERENCES `board_comment` (`id`)
) ENGINE = InnoDB;
