package me.mythicalsystems.mcpanelxcore.database;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.omg.IOP.MultipleComponentProfileHelper;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class connection {
    private Connection connection;

    public Connection getConnection() throws  SQLException {
        if (connection != null) {
            return connection;
        }

        String url = "jdbc:mysql://" + McPanelX_Core.config.getString("Database.host")+"/"+McPanelX_Core.config.getString("Database.database");
        String user = McPanelX_Core.config.getString("Database.username");
        String password = McPanelX_Core.config.getString("Database.password");

        Connection connection = DriverManager.getConnection(url,user,password);
        this.connection = connection;
        Bukkit.getLogger().info("[MCPanelX-Core] Connected to the MySQL server!");
        return connection;
    }

    public void initializeDatabase() throws  SQLException {
        Statement statement = getConnection().createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS `"+McPanelX_Core.config.getString("Database.database")+"`.`mcpanelx_core_logs` (`id` INT NOT NULL AUTO_INCREMENT , `uuid` TEXT NOT NULL , `name` TEXT NOT NULL , `server_name` TEXT NOT NULL, `type` ENUM('command','chat') NOT NULL , `value` TEXT NOT NULL , `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
        statement.execute(sql);
        statement.close();
    }

    public void insertChatLog(String uuid, String username, String value) throws SQLException {
        if (uuid == null || username == null || value == null) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot insert chat log with null values!");
        }
        //Bukkit.getLogger().info("[McPanelX-Core] ("+uuid+") "+username+": "+value);
        String sql = "INSERT INTO `mcpanelx_core_logs` (`uuid`, `name`, `server_name`, `type`, `value`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(sql);
        statement.setString(1, uuid.toString());
        statement.setString(2, username.toString());
        statement.setString(3, McPanelX_Core.config.getString("Panel.server_name").toString());
        statement.setString(4, "chat");
        statement.setString(5, value.toString());
        try {
            statement.execute();
        } catch (Exception e) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot insert chat log! "+e.toString());
        }
        statement.close();
    }


    public void insertCommandLog(String uuid, String username, String value) throws SQLException {
        if (uuid == null || username == null || value == null) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot insert chat log with null values!");
        }
        String sql = "INSERT INTO `mcpanelx_core_logs` (`uuid`, `name`, `server_name`, `type`, `value`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(sql);
        statement.setString(1, uuid);
        statement.setString(2, username);
        statement.setString(3, McPanelX_Core.config.getString("Panel.server_name"));
        statement.setString(4, "command");
        statement.setString(5, value);
        try {
            statement.execute();
        } catch (Exception e) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot insert command log! "+e.toString());
        }
        statement.close();
    }
}
