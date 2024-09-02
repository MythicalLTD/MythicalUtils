CREATE TABLE
    IF NOT EXISTS `mcpanelx_users` (
        `id` INT NOT NULL AUTO_INCREMENT,
        `uuid` TEXT NOT NULL,
        `token` TEXT NOT NULL,
        `username` TEXT NOT NULL,
        `discord_id` TEXT NOT NULL DEFAULT 'None',
        `discord_pin` TEXT NOT NULL DEFAULT 'None',
        `online` INT NOT NULL DEFAULT '0',
        `server` TEXT NOT NULL DEFAULT 'None',
        `brand_name` TEXT NOT NULL DEFAULT 'None',
        `version_name` TEXT NOT NULL DEFAULT 'None',
        `last_ip` TEXT NOT NULL DEFAULT '127.0.0.1',
        `first_ip` TEXT NOT NULL DEFAULT '127.0.0.1',
        `last_seen` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `first_seen` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (`id`)
    ) ENGINE = InnoDB;