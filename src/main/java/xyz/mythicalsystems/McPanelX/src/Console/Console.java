package xyz.mythicalsystems.McPanelX.src.Console;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import xyz.mythicalsystems.McPanelX.McPanelX;
import xyz.mythicalsystems.McPanelX.MinecraftPlugin;

public class Console {

    public static void run() {
        try {
            Connection connection = McPanelX.connection;
            if (!isConsoleTableEmpty()) {
                ResultSet result = getConsoleTable();
                try {
                    while (result.next()) {
                        String command = result.getString("cmd");
                        MinecraftPlugin.getInstance().getProxy().getScheduler().runAsync(MinecraftPlugin.getInstance(), () -> {
                            MinecraftPlugin.getInstance().getProxy().getPluginManager().dispatchCommand(
                                    MinecraftPlugin.getInstance().getProxy().getConsole(), command);
                        });
                        PreparedStatement statement = connection
                                .prepareStatement("DELETE FROM `mcpanelx_console` WHERE `cmd` = ?");
                        statement.setString(1, command);
                        statement.executeUpdate();
                        statement.close();
                    }
                } catch (Exception e) {
                    McPanelX.logger.error("Console", "Error while tried to run the console: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            McPanelX.logger.error("Console", "Error while tried to run the console: " + e.getMessage());
        }
    }

    /**
     * Is the console table empty?
     * 
     * @return boolean
     */
    public static boolean isConsoleTableEmpty() {
        Connection connection = McPanelX.connection;

        String query = "SELECT * FROM `mcpanelx_console`";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            McPanelX.logger.error("Console",
                    "Error while tried to check if the console table is empty: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get the console table
     * 
     * @return ResultSet
     */
    public static ResultSet getConsoleTable() {
        Connection connection = McPanelX.connection;

        String query = "SELECT * FROM `mcpanelx_console`";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            return result;
        } catch (Exception e) {
            McPanelX.logger.error("Console", "Error while tried to get the console table: " + e.getMessage());
            return null;
        }
    }
}
