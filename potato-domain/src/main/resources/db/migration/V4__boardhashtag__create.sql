CREATE TABLE `board_hash_tag`
(
    `id`                      BIGINT      NOT NULL AUTO_INCREMENT,
    `type`                    VARCHAR(30) NOT NULL,
    `board_id`                BIGINT      NOT NULL,
    `member_id`               BIGINT      NOT NULL,
    `hash_tag`                VARCHAR(50) NOT NULL,
    `created_date_time`       DATETIME(6) DEFAULT NULL,
    `last_modified_date_time` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_board_hash_tag_1` (`board_id`, `type`)
) ENGINE = InnoDB;
