CREATE TABLE IF NOT EXISTS `board_image`
(
    `id`                      BIGINT        NOT NULL AUTO_INCREMENT,
    `board_id`                BIGINT        NOT NULL,
    `type`                    VARCHAR(30)   NOT NULL,
    `image_url`               VARCHAR(2048) NOT NULL,
    `created_date_time`       DATETIME(6) DEFAULT NULL,
    `last_modified_date_time` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;

ALTER TABLE `organization_board`
    DROP `image_url`;

ALTER TABLE `admin_board`
    DROP `image_url`;

ALTER TABLE `delete_organization_board`
    DROP `image_url`;
