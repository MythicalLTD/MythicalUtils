package xyz.mythicalsystems.mcpanelxcore.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.mcpanelxcore.drivers.MySQL;

public class LoggerHelper {

    /**
     * Log a chat message from a player
     * 
     * @param player  Player
     * @param message The message
     * 
     * @throws SQLException
     */
    public static void logChatMessage(ProxiedPlayer player, String message) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String serverName = player.getServer().getInfo().getName();
        String query = "INSERT INTO " + MySQL.logs_table + " (servername, uuid, type, value) VALUES (?, ?, 'chat', ?)";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, serverName);
            statement.setString(2, player.getUniqueId().toString());
            statement.setString(3, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Log a command that was executed by a player
     * 
     * @param player  Player
     * @param command The command
     * 
     * @throws SQLException
     */
    public static void logCommand(ProxiedPlayer player, String command) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String serverName = player.getServer().getInfo().getName();
        String query = "INSERT INTO " + MySQL.logs_table
                + " (servername, uuid, type, value) VALUES (?, ?, 'command', ?)";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, serverName);
            statement.setString(2, player.getUniqueId().toString());
            statement.setString(3, command);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
}
