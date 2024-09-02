CREATE TABLE
    IF NOT EXISTS `mcpanelx_logins` (
        `id` INT NOT NULL AUTO_INCREMENT,
        `uuid` TEXT NOT NULL,
        `ip` TEXT NOT NULL,
        `client_name` TEXT NOT NULL,
        `client_version` TEXT NOT NULL,
        `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (`id`)
    ) ENGINE = InnoDB;