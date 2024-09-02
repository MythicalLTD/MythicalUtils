package xyz.mythicalsystems.McPanelX.src.Link;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import xyz.mythicalsystems.McPanelX.McPanelX;

public class Discord {
    /**
     * Register a pin
     */
    public static void registerPin(UUID uuid, String pin) {
        if (Account.doesUUIDExist(uuid)) {
            Connection connection = McPanelX.connection;
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE `mcpanelx_users` SET `discord_pin` = ? WHERE `uuid` = ?;");
                statement.setString(1, pin);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            } catch (Exception e) {
                McPanelX.logger.error("Discord", "Failed to register pin");
            }

        } else {
            McPanelX.logger.error("Discord", "UUID does not exist!");
        }
    }

    /**
     * Get a pin
     */
    public static String getPin(UUID uuid) {
        if (Account.doesUUIDExist(uuid)) {
            Connection connection = McPanelX.connection;
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT `discord_pin` FROM `mcpanelx_users` WHERE `uuid` = ?;");
                statement.setString(1, uuid.toString());
                return statement.executeQuery().getString("discord_pin");
            } catch (Exception e) {
                McPanelX.logger.error("Discord", "Failed to get pin");
                return null;
            }
        } else {
            McPanelX.logger.error("Discord", "UUID does not exist!");
            return null;
        }
    }

    /**
     * Remove a pin
     * 
     * @param uuid
     */
    public static void removePin(UUID uuid) {
        if (Account.doesUUIDExist(uuid)) {
            Connection connection = McPanelX.connection;
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE `mcpanelx_users` SET `discord_pin` = 'None' WHERE `uuid` = ?;");
                statement.setString(1, uuid.toString());
                statement.executeUpdate();
            } catch (Exception e) {
                McPanelX.logger.error("Discord", "Failed to remove pin");
            }
        } else {
            McPanelX.logger.error("Discord", "UUID does not exist!");
        }
    }
    /**
     * Remove a pin
     * 
     * @param discord_id
     */
    public static void removePin(String discord_id) {
        if (doesDiscordExist(discord_id)) {
            Connection connection = McPanelX.connection;
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE `mcpanelx_users` SET `discord_pin` = 'None' WHERE `discord_id` = ?;");
                statement.setString(1, discord_id);
                statement.executeUpdate();
            } catch (Exception e) {
                McPanelX.logger.error("Discord", "Failed to remove pin");
            }
        } else {
            McPanelX.logger.error("Discord", "Discord does not exist!");
        }
    }

    /**
     * Is an account linked?
     * 
     * @param uuid
     * @return
     */
    public static boolean isAccountLinked(UUID uuid) {  
        if (Account.doesUUIDExist(uuid)) {
            Connection connection = McPanelX.connection;
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT `discord_id` FROM `mcpanelx_users` WHERE `uuid` = ?;");
                statement.setString(1, uuid.toString());
                try {
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        return !resultSet.getString("discord_id").equals("None");
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    McPanelX.logger.error("Discord", "(isAccountLinked) Failed to check if account is linked" + e.getMessage());
                    return false;
                }
            } catch (Exception e) {
                McPanelX.logger.error("Discord", "(isAccountLinked) Failed to check if account is linked"+e.getMessage());
                return false;
            }
        } else {
            McPanelX.logger.error("Discord", "UUID does not exist!");
            return false;
        }
    }

    /**
     * Is an account linked?
     * 
     * @param discord_id
     * @return
     */
    public static boolean isAccountLinked(String discord_id) {

        Connection connection = McPanelX.connection;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `uuid` FROM `mcpanelx_users` WHERE `discord_id` = ?;");
            statement.setString(1, discord_id);

            return statement.executeQuery().next();
        } catch (Exception e) {
            McPanelX.logger.error("Discord", "Failed to check if account is linked");
            return false;
        }
    }

    /**
     * Does a pin exist?
     * 
     * @param pin
     * @return
     */
    public static boolean doesPinExist(String pin) {
        Connection connection = McPanelX.connection;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `uuid` FROM `mcpanelx_users` WHERE `discord_pin` = ?;");
            statement.setString(1, pin);
            return statement.executeQuery().next();
        } catch (Exception e) {
            McPanelX.logger.error("Discord", "Failed to check if pin exists");
            return false;
        }
    }

    /**
     * Generate a pin
     * 
     * @return
     */
    public static String generatePin() {
        return String.valueOf((int) ((Math.random() * 9000) + 1000));
    }
    /**
     * Register a discord
     * 
     * @param pin
     * @param discord_id
     */
    public static void registerDiscord(String pin, String discord_id) {
        if (doesPinExist(pin)) {
            Connection connection = McPanelX.connection;
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE `mcpanelx_users` SET `discord_id` = ? WHERE `discord_pin` = ?;");
                statement.setString(1, discord_id);
                statement.setString(2, pin);
                statement.executeUpdate();
            } catch (Exception e) {
                McPanelX.logger.error("Discord", "Failed to register discord");
            }
        } else {
            McPanelX.logger.error("Discord", "Pin does not exist!");
        }
    }   
    /**
     * Unlink a discord
     * 
     * @param discord_id
     */
    public static void unLinkDiscord(String discord_id) {
        if (doesDiscordExist(discord_id)) {
            Connection connection = McPanelX.connection;
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE `mcpanelx_users` SET `discord_id` = 'None' WHERE `discord_id` = ?;");
                statement.setString(1, discord_id);
                statement.executeUpdate();
            } catch (Exception e) {
                McPanelX.logger.error("Discord", "Failed to unlink discord");
            }
        } else {
            McPanelX.logger.error("Discord", "Discord does not exist!");
        }
    }
    /**
     * Unlink a discord
     * 
     * @param uuid
     */
    public static void unLinkDiscord(UUID uuid) {
        if (Account.doesUUIDExist(uuid)) {
            Connection connection = McPanelX.connection;
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE `mcpanelx_users` SET `discord_id` = 'None' WHERE `uuid` = ?;");
                statement.setString(1, uuid.toString());
                statement.executeUpdate();
            } catch (Exception e) {
                McPanelX.logger.error("Discord", "Failed to unlink discord");
            }
        } else {
            McPanelX.logger.error("Discord", "UUID does not exist!");
        }
    }
    /**
     * Does this discord user exist?
     * 
     * @param discord_id
     * @return
     */
    public static boolean doesDiscordExist(String discord_id) {
        Connection connection = McPanelX.connection;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `uuid` FROM `mcpanelx_users` WHERE `discord_id` = ?;");
            statement.setString(1, discord_id);
            return statement.executeQuery().next();
        } catch (Exception e) {
            McPanelX.logger.error("Discord", "Failed to check if discord exists");
            return false;
        }
    }
}
