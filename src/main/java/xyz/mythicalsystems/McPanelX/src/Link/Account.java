package xyz.mythicalsystems.McPanelX.src.Link;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.McPanelX.McPanelX;
import xyz.mythicalsystems.McPanelX.src.Messages.Messages;
import xyz.mythicalsystems.McPanelX.src.Translators.ChatTranslator;
import xyz.mythicalsystems.McPanelX.src.Translators.VersionTranslator;

public class Account {

    public static void onJoin(ProxiedPlayer player, int version, String brandName) {
        // Get the UUID of the player
        UUID uuid = player.getUniqueId();
        // Get the IP address of the player
        InetSocketAddress socketAddress = (InetSocketAddress) player.getSocketAddress();
        String ipAddress = socketAddress.getAddress().getHostAddress();

        // Get the server the player is on
        String server_name = player.getServer().getInfo().getName();

        // Get the player name
        String player_name = player.getName();

        // Check if the player is in the database
        if (!doesUUIDExist(uuid)) {
            // If the player is not in the database, add them
            try {

                Connection connection = McPanelX.connection;
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO `mcpanelx_users` (`uuid`, `token`, `username`, `server`, `brand_name`,`version_name`, `last_ip`, `first_ip`) VALUES (?, ?, ?, ?, ?, ?, ?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, generateUserToken());
                statement.setString(3, player_name);
                statement.setString(4, server_name);
                statement.setString(5, brandName);
                statement.setString(6, VersionTranslator.translate(version));
                statement.setString(7, ipAddress);
                statement.setString(8, ipAddress);
                statement.executeUpdate();
                statement.close();

                updateLastSeen(player);
                updateVersion(player, version);
                updateBrandName(player, brandName);
                updateServer(player);
                updateIP(player);
                markPlayerAsOnline(player);
                Logins.add(uuid, ipAddress, brandName, VersionTranslator.translate(version));
                //McPanelX.logger.info("Account",  "Added player to users list"+generatePin());
            } catch (Exception e) {
                McPanelX.logger.error("Account", "Failed to add player to users list"+e.getMessage());
                player.disconnect(new TextComponent(ChatTranslator.Translate(Messages.getMessage().getString("Error.ErrorOnJoin"))));
            }
        } else {
            // If the player is in the database, update their information
            PinLogin.onJoin(player);
            updateLastSeen(player);
            updateVersion(player, version);
            updateBrandName(player, brandName);
            updateServer(player);
            updateIP(player);
            markPlayerAsOnline(player);
            Logins.add(uuid, ipAddress, brandName, VersionTranslator.translate(version));
        }

    }

    /**
     * When a player leaves the server
     * 
     * @param player
     */
    public static void onLeave(ProxiedPlayer player) {
        if (!doesUUIDExist(player.getUniqueId())) {
            return;
        } else {
            markPlayerAsOffline(player);
            updateLastSeen(player);
        }
    }

    /**
     * Mark a player as online
     * 
     * @param player
     */
    public static void markPlayerAsOnline(ProxiedPlayer player) {
        if (doesUUIDExist(player.getUniqueId())) {
            try {

                Connection connection = McPanelX.connection;
                PreparedStatement statement = connection
                        .prepareStatement("UPDATE `mcpanelx_users` SET `online` = 1 WHERE `uuid` = ?");
                statement.setString(1, player.getUniqueId().toString());
                statement.executeUpdate();
                statement.close();

            } catch (Exception e) {
                McPanelX.logger.error("Account", "(markPlayerAsOnline) Failed to connect to the database");
            }
        }
    }

    /**
     * Mark a player as offline
     * 
     * @param player
     */
    public static void markPlayerAsOffline(ProxiedPlayer player) {
        if (doesUUIDExist(player.getUniqueId())) {
            try {

                Connection connection = McPanelX.connection;
                PreparedStatement statement = connection
                        .prepareStatement("UPDATE `mcpanelx_users` SET `online` = 0 WHERE `uuid` = ?");
                statement.setString(1, player.getUniqueId().toString());
                statement.executeUpdate();
                statement.close();

            } catch (Exception e) {
                McPanelX.logger.error("Account", "(markPlayerAsOffline) Failed to connect to the database");
            }
        }
    }

    /**
     * Update the last seen time of a player
     * 
     * @param player
     */
    public static void updateLastSeen(ProxiedPlayer player) {
        if (doesUUIDExist(player.getUniqueId())) {
            try {

                Connection connection = McPanelX.connection;
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE `mcpanelx_users` SET `last_seen` = CURRENT_TIMESTAMP WHERE `uuid` = ?");

                statement.setString(1, player.getUniqueId().toString());
                statement.executeUpdate();
                statement.close();
                
            } catch (Exception e) {
                McPanelX.logger.error("Account", "(updateLastSeen) Failed to connect to the database");
            }
        }
    }

    /**
     * Update the version of a player
     * 
     * @param player
     * @param version
     */
    public static void updateVersion(ProxiedPlayer player, int version) {
        if (doesUUIDExist(player.getUniqueId())) {
            try {

                Connection connection = McPanelX.connection;
                PreparedStatement statement = connection
                        .prepareStatement("UPDATE `mcpanelx_users` SET `version_name` = ? WHERE `uuid` = ?");
                statement.setString(1, VersionTranslator.translate(version));
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
                statement.close();

            } catch (Exception e) {
                McPanelX.logger.error("Account", "(updateVersion) Failed to connect to the database");
            }
        }
    }

    /**
     * Update the brand name of a player
     * 
     * @param player
     * @param brandName
     */
    public static void updateBrandName(ProxiedPlayer player, String brandName) {
        if (doesUUIDExist(player.getUniqueId())) {
            try {

                Connection connection = McPanelX.connection;
                PreparedStatement statement = connection
                        .prepareStatement("UPDATE `mcpanelx_users` SET `brand_name` = ? WHERE `uuid` = ?");
                statement.setString(1, brandName);
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
                statement.close();

            } catch (Exception e) {
                McPanelX.logger.error("Account", "(updateBrandName) Failed to connect to the database");
            }
        }
    }

    /**
     * Update the server of a player
     * 
     * @param player
     */
    public static void updateServer(ProxiedPlayer player) {
        if (doesUUIDExist(player.getUniqueId())) {
            try {

                Connection connection = McPanelX.connection;
                PreparedStatement statement = connection
                        .prepareStatement("UPDATE `mcpanelx_users` SET `server` = ? WHERE `uuid` = ?");
                statement.setString(1, player.getServer().getInfo().getName());
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
                statement.close();

            } catch (Exception e) {
                McPanelX.logger.error("Account", "(updateServer) Failed to connect to the database");
            }
        }
    }

    /**
     * Update the IP of a player
     * 
     * @param player
     */
    public static void updateIP(ProxiedPlayer player) {
        if (doesUUIDExist(player.getUniqueId())) {
            try {

                Connection connection = McPanelX.connection;
                PreparedStatement statement = connection
                        .prepareStatement("UPDATE `mcpanelx_users` SET `last_ip` = ? WHERE `uuid` = ?");
                statement.setString(1, ((InetSocketAddress) player.getSocketAddress()).getAddress().getHostAddress());
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
                statement.close();

            } catch (Exception e) {
                McPanelX.logger.error("Account", "(updateIP) Failed to connect to the database");
            }
        }
    }

    public static void updateUser(UUID uuid, String info, String value) {
        if (doesUUIDExist(uuid)) {
            try {

                Connection connection = McPanelX.connection;
                PreparedStatement statement = connection
                        .prepareStatement("UPDATE `mcpanelx_users` SET `" + info + "` = ? WHERE `uuid` = ?");
                statement.setString(1, value);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
                statement.close();

            } catch (Exception e) {
                McPanelX.logger.error("Account", "(updateUser) Failed to connect to the database");
            }
        }
    }

    /**
     * Check if a UUID exists in the database
     * 
     * @param uuid The UUID to check
     * @return
     */
    public static boolean doesUUIDExist(UUID uuid) {
        try {

            ResultSet result = McPanelX.connection.createStatement()
                    .executeQuery("SELECT * FROM `mcpanelx_users` WHERE `uuid` = '" + uuid + "'");

            if (!result.next()) {
                result.close();

                return false;
            } else {
                result.close();

                return true;
            }
        } catch (Exception e) {
            McPanelX.logger.error("Account", "(doesUUIDExist) Failed to connect to the database!" + e.getMessage());
            return false;
        }
    }

    /**
     * Generate a user token
     * 
     * @return String
     */
    public static String generateUserToken() {
        String token = "mcpanelx_" + Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()) + "_"
                + Base64.getEncoder().encodeToString(new Date().toString().getBytes()) + "_token";
        return token;
    }
    /**
     * Get info about the user
     * 
     * @param uuid
     * @param info
     * @return
     */
    public static String getUserInfo(UUID uuid, String info) {
        if (doesUUIDExist(uuid)) {
            try {

                ResultSet result = McPanelX.connection.createStatement()
                        .executeQuery("SELECT * FROM `mcpanelx_users` WHERE `uuid` = '" + uuid + "'");
                if (result.next()) {
                    return result.getString(info);
                } else {
                    return null;
                }
            } catch (Exception e) {
                McPanelX.logger.error("Account", "(getUserInfo) Failed to connect to the database");
                return null;
            }
        } else {
            return null;
        }
    }
    /**
     * Mark the hole server as offline
     * 
     */
    public static void markHoleServerAsOffline() {
        try {

            Connection connection = McPanelX.connection;
            PreparedStatement statement = connection.prepareStatement("UPDATE `mcpanelx_users` SET `online` = 0");
            statement.executeUpdate();
            statement.close();

        } catch (Exception e) {
            McPanelX.logger.error("Account", "(markHoleServerAsOffline) Failed to connect to the database");
        }
    }

    /**
     * Check if a user exists
     * 
     * @param token The token to check
     * 
     * @return boolean
     */
    public static boolean doesUserExist(String token) {
        try {
            PreparedStatement statement = McPanelX.connection.prepareStatement(
                    "SELECT * FROM `mcpanelx_users` WHERE `token` = ?");
            statement.setString(1, token);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                result.close();

                return false;
            } else {
                result.close();

                return true;
            }
        } catch (Exception e) {
            McPanelX.logger.error("Account", "(doesUserExist) Failed to connect to the database");
            return false;
        }
    }
}
