package xyz.mythicalsystems.McPanelX.src.MySQL;


import java.sql.ResultSet;

import com.zaxxer.hikari.HikariDataSource;

import xyz.mythicalsystems.McPanelX.McPanelX;
import xyz.mythicalsystems.McPanelX.MinecraftPlugin;
import xyz.mythicalsystems.McPanelX.src.Config.Config;

public class MySQLConnector {
    private HikariDataSource hikari;

    public MySQLConnector(String host, String port, String database, String username, String password) {
        hikari = new HikariDataSource();
        hikari.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikari.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikari.setUsername(username);
        hikari.setPassword(password);
        hikari.setMaximumPoolSize(10);
        hikari.setMinimumIdle(5);
        hikari.setConnectionTimeout(30000);
        hikari.setIdleTimeout(600000);
        hikari.setMaxLifetime(1800000);
        hikari.setLeakDetectionThreshold(1800000);
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    public void close() {
        hikari.close();
    }

    public void reconnect() {
        hikari.close();
        hikari = new HikariDataSource(hikari);
    }   

    public void tryConnection() {
        try {
            hikari.getConnection().close();
            McPanelX.logger.info("MySQLConnector", "Connected to MySQL server");
            String version = Config.getSetting().getString("Database.Version");
            
            // Create the migration table ;)
            String query = "CREATE TABLE IF NOT EXISTS `mcpanelx_migrations` (`id` INT NOT NULL AUTO_INCREMENT , `version` TEXT NOT NULL , `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
            hikari.getConnection().createStatement().executeUpdate(query);

            // Check if the structure is up to date
            if (version != null) {
                McPanelX.logger.info("MySQLConnector", "Running McPanelX database version: " + version);
                if (version.equals("1.0.0")) {
                    ResultSet result = hikari.getConnection().createStatement().executeQuery("SELECT * FROM `mcpanelx_migrations` WHERE `version` = '1.0.0';");
                    if (!result.next()) {
                        query = "INSERT INTO `mcpanelx_migrations` (`version`) VALUES ('1.0.0');";
                        hikari.getConnection().createStatement().executeUpdate(query);
                        query = "CREATE TABLE IF NOT EXISTS `mcpanelx_users` (`id` INT NOT NULL AUTO_INCREMENT , `uuid` TEXT NOT NULL ,`token` TEXT NOT NULL , `username` TEXT NOT NULL , `online` INT NOT NULL DEFAULT '0', `server` TEXT NOT NULL DEFAULT 'None', `brand_name` TEXT NOT NULL DEFAULT 'None' , `version_name` TEXT NOT NULL DEFAULT 'None', `last_ip` TEXT NOT NULL DEFAULT '127.0.0.1' , `first_ip` TEXT NOT NULL DEFAULT '127.0.0.1' , `last_seen` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , `first_seen` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
                        hikari.getConnection().createStatement().executeUpdate(query);
                        query = "CREATE TABLE IF NOT EXISTS `mcpanelx_logins` (`id` INT NOT NULL AUTO_INCREMENT , `uuid` TEXT NOT NULL , `ip` TEXT NOT NULL , `client_name` TEXT NOT NULL , `client_version` TEXT NOT NULL , `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
                        hikari.getConnection().createStatement().executeUpdate(query);
                        query = "CREATE TABLE IF NOT EXISTS `mcpanelx_logs` (`id` INT NOT NULL AUTO_INCREMENT , `servername` TEXT NOT NULL, `uuid` TEXT NOT NULL , `type` ENUM('command','chat') NOT NULL , `value` TEXT NOT NULL , `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
                        hikari.getConnection().createStatement().executeUpdate(query);
                    } else {
                        McPanelX.logger.info("MySQLConnector", "Database is up to date");
                    }
                }
            }
            hikari.getConnection().close();

        } catch (Exception e) {
            McPanelX.logger.error("MySQLConnector", "Failed to connect to MySQL server: " + e.getMessage());
            MinecraftPlugin.getInstance().getProxy().stop();
        }
    }

}   
