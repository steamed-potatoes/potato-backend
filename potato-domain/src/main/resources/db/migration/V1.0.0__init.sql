CREATE TABLE IF NOT EXISTS `spring_session`
(
    `session_id`            CHAR(36) NOT NULL,
    `creation_time`         BIGINT   NOT NULL,
    `last_access_time`      BIGINT   NOT NULL,
    `max_inactive_interval` INT      NOT NULL,
    `principal_name`        VARCHAR(100),
    CONSTRAINT `spring_session_pk` PRIMARY KEY (`session_id`)
);

CREATE INDEX IF NOT EXISTS `spring_session_ix1` ON `spring_session` (`last_access_time`);

CREATE TABLE IF NOT EXISTS `spring_session_attributes`
(
    `session_id`      CHAR(36)     NOT NULL,
    `attribute_name`  VARCHAR(200) NOT NULL,
    `attribute_bytes` MEDIUMBLOB   NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (`session_id`, `attribute_name`),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (`session_id`) REFERENCES `spring_session` (`session_id`) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `spring_session_attributes_ix1` ON `spring_session_attributes` (`session_id`);

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
    KEY `idx_organization_1` (`category`)
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
