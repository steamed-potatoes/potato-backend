CREATE TABLE IF NOT EXISTS SPRING_SESSION
(
    SESSION_ID            CHAR(36) NOT NULL,
    CREATION_TIME         BIGINT   NOT NULL,
    LAST_ACCESS_TIME      BIGINT   NOT NULL,
    MAX_INACTIVE_INTERVAL INT      NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (SESSION_ID)
);

CREATE INDEX IF NOT EXISTS SPRING_SESSION_IX1 ON SPRING_SESSION (LAST_ACCESS_TIME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES
(
    SESSION_ID      CHAR(36)     NOT NULL,
    ATTRIBUTE_NAME  VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES MEDIUMBLOB   NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_ID) REFERENCES SPRING_SESSION (SESSION_ID) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS SPRING_SESSION_ATTRIBUTES_IX1 ON SPRING_SESSION_ATTRIBUTES (SESSION_ID);

CREATE TABLE IF NOT EXISTS `member`
(
    `id`                      bigint      NOT NULL AUTO_INCREMENT,
    `email`                   varchar(50) NOT NULL,
    `name`                    varchar(50) NOT NULL,
    `profile_url`             varchar(2048) DEFAULT NULL,
    `major`                   varchar(30)   DEFAULT NULL,
    `class_number`            integer(9)    DEFAULT NULL,
    `provider`                varchar(30) NOT NULL,
    `created_date_time`       datetime(6)   DEFAULT NULL,
    `last_modified_date_time` datetime(6)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_member_1` (`email`)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `administrator`
(
    `id`                      bigint      NOT NULL AUTO_INCREMENT,
    `email`                   varchar(50) NOT NULL,
    `name`                    varchar(50) NOT NULL,
    `created_date_time`       datetime(6) DEFAULT NULL,
    `last_modified_date_time` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_administrator_1` (`email`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `organization`
(
    `id`                      bigint      NOT NULL AUTO_INCREMENT,
    `sub_domain`              varchar(50) NOT NULL,
    `name`                    varchar(50) NOT NULL,
    `category`                varchar(30) NOT NULL,
    `profile_url`             varchar(2048)        DEFAULT NULL,
    `description`             varchar(255)         DEFAULT NULL,
    `members_count`           integer     NOT NULL DEFAULT 0,
    `followers_count`         integer     NOT NULL DEFAULT 0,
    `created_date_time`       datetime(6)          DEFAULT NULL,
    `last_modified_date_time` datetime(6)          DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_organization_1` (`sub_domain`),
    KEY `idx_organization_1` (`category`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `organization_member_mapper`
(
    `id`                      bigint      NOT NULL AUTO_INCREMENT,
    `organization_id`         bigint      NOT NULL,
    `member_id`               bigint      NOT NULL,
    `role`                    varchar(30) NOT NULL,
    `created_date_time`       datetime(6) DEFAULT NULL,
    `last_modified_date_time` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`),
    KEY `idx_organization_member_mapper_1` (`member_id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `organization_follower`
(
    `id`                      bigint NOT NULL AUTO_INCREMENT,
    `organization_id`         bigint NOT NULL,
    `member_id`               bigint NOT NULL,
    `created_date_time`       datetime(6) DEFAULT NULL,
    `last_modified_date_time` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`)
) ENGINE = InnoDB;
