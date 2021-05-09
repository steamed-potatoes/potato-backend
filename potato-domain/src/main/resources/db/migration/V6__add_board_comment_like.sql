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
