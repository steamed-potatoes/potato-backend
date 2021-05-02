ALTER TABLE `organization_board`
    DROP INDEX `idx_organization_board_2`;

ALTER TABLE `organization`
    DROP INDEX `idx_organization_2`;

ALTER TABLE `organization`
    ADD INDEX `idx_organization_2` (`followers_count`, `id`);

ALTER TABLE `organization_board`
    ADD INDEX `idx_organization_board_2` (`likes_count`, `id`);
