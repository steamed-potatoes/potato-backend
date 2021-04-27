ALTER TABLE `organization`
    ADD INDEX `idx_organization_2` (`followers_count`);

ALTER TABLE `organization_board`
    ADD INDEX `idx_organization_board_2` (`likes_count`);
