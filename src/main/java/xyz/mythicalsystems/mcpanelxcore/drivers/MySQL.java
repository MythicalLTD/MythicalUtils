package xyz.mythicalsystems.mcpanelxcore.drivers;

import java.sql.*;

import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;

public class MySQL {
    private Connection connection;
    public static String users_table = "mcpanelx_users";
    public static String logs_table = "mcpanelx_logs";

    /**
     * Get the connection to the MySQL database
     * 
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        String host = McPanelX_Core.cfg().getString("Database.host");
        String port = McPanelX_Core.cfg().getString("Database.port");
        String database = McPanelX_Core.cfg().getString("Database.database");
        String username = McPanelX_Core.cfg().getString("Database.username");
        String password = McPanelX_Core.cfg().getString("Database.password");

        if (host == null || port == null || database == null || username == null) {
            McPanelX_Core.getInstance().getLogger()
                    .info("[McPanelX-Core] Failed to get MySQL connection details from config!");
            McPanelX_Core.getInstance().getProxy().stop("Failed to get MySQL connection details from config!");
            SQLException e = new SQLException("Failed to get MySQL connection details from config!");
            throw e;
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username,
                    password);
            return connection;
        } catch (SQLException e) {
            McPanelX_Core.getInstance().getLogger().info("[McPanelX-Core] Failed to connect to MySQL database: " + e);
            throw e;
        } 
    }

    /**
     * Try to connect to the MySQL database
     * 
     * @return boolean
     */
    public boolean TryConnection() {
        try {
            getConnection();
            initializeDatabase();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Initialize the MySQL database
     * 
     * @throws SQLException
     */
    public void initializeDatabase() throws SQLException {
        try {
            Statement statement = getConnection().createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS `"+MySQL.users_table+"` (`id` INT NOT NULL AUTO_INCREMENT , `uuid` TEXT NOT NULL , `name` TEXT NOT NULL , `token` TEXT NOT NULL , `online` ENUM('online','offline') NOT NULL , `last_ip` TEXT NOT NULL , `first_ip` TEXT NOT NULL , `last_seen` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , `first_seen` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
            String sql2 = "CREATE TABLE IF NOT EXISTS `"+MySQL.logs_table+"`  (`id` INT NOT NULL AUTO_INCREMENT , `servername` TEXT NOT NULL, `uuid` TEXT NOT NULL , `type` ENUM('command','chat', 'console') NOT NULL , `value` TEXT NOT NULL , `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
            statement.execute(sql);
            statement.execute(sql2);
            statement.close();
        } catch (SQLException e) {
            McPanelX_Core.getInstance().getLogger().info("[McPanelX-Core] Failed to create table: " + e);
            throw e;
        }
    }


}
