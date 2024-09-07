CREATE TABLE
    IF NOT EXISTS `mcpanelx_console` (
        `id` INT NOT NULL AUTO_INCREMENT,
        `cmd` TEXT NOT NULL,
        `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (`id`)
    ) ENGINE = InnoDB;