CREATE TABLE
    IF NOT EXISTS `mcpanelx_logs` (
        `id` INT NOT NULL AUTO_INCREMENT,
        `servername` TEXT NOT NULL,
        `uuid` TEXT NOT NULL,
        `type` ENUM ('command', 'chat') NOT NULL,
        `value` TEXT NOT NULL,
        `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (`id`)
    ) ENGINE = InnoDB;