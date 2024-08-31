package xyz.mythicalsystems.McPanelX.src.Link;

import java.sql.Connection;
import java.sql.PreparedStatement;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import xyz.mythicalsystems.McPanelX.McPanelX;

public class Logger {
    public static void logCommand(ProxiedPlayer player, String message) {
        Connection connection = McPanelX.connection;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO mcpanelx_logs (servername, uuid, type, value) VALUES (?, ?, ?, ?)");
            statement.setString(1, player.getServer().getInfo().getName());
            statement.setString(2, player.getUniqueId().toString());
            statement.setString(3, "command");
            statement.setString(4, message);
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            McPanelX.logger.error("Logger", "Error while tried to save the player info: " + e.getMessage());
        }
        // Log the command
    }

    public static void logChat(ProxiedPlayer player, String message) {
        Connection connection = McPanelX.connection;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO mcpanelx_logs (servername, uuid, type, value) VALUES (?, ?, ?, ?)");
            statement.setString(1, player.getServer().getInfo().getName());
            statement.setString(2, player.getUniqueId().toString());
            statement.setString(3, "chat");
            statement.setString(4, message);
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            McPanelX.logger.error("Logger", "Error while tried to save the player info: " + e.getMessage());
        }
    }
}
