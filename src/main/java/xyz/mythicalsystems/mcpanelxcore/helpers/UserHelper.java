package xyz.mythicalsystems.mcpanelxcore.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.mcpanelxcore.drivers.MySQL;
import java.util.Base64;
import java.util.Date;

public class UserHelper extends MySQL {

    /**
     * Create a new user in the database!
     * 
     * @param username Username
     * @param uuid     UUID
     * @param token    Token
     * @param ip       IP
     * 
     * @throws SQLException
     */
    public static void CreateUser(String username, UUID uuid, String ip) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String query = "INSERT INTO " + MySQL.users_table + " (name,uuid,token,last_ip,first_ip) VALUES (?,?,?,?,?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, uuid.toString());
            statement.setString(3, generateUserToken());
            statement.setString(4, ip);
            statement.setString(5, ip);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Check if the user is valid
     * 
     * @param uuid UUID
     * 
     * @return boolean
     * 
     * @throws SQLException
     */
    public static boolean isUserValid(UUID uuid) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String query = "SELECT 1 FROM " + MySQL.users_table + " WHERE uuid = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw e; // or handle the exception as needed
        }
    }

    /**
     * Mark the user as offline
     * 
     * @param uuid
     * @throws SQLException
     */
    public static void markUserAsOffline(UUID uuid) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String query = "UPDATE `" + MySQL.users_table + "` SET `online` = 'offline' WHERE `" + MySQL.users_table
                + "`.`uuid` = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Mark the user as online
     * 
     * @param uuid
     * @throws SQLException
     */
    public static void markUserAsOnline(UUID uuid) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String query = "UPDATE `" + MySQL.users_table + "` SET `online` = 'online' WHERE `" + MySQL.users_table
                + "`.`uuid` = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Mark the hole server as offline
     * 
     * @throws SQLException
     */
    public static void markHoleServerAsOffline() throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String query = "UPDATE `" + MySQL.users_table + "` SET `online` = 'offline'";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Update the user last seen
     * 
     * @param uuid
     * 
     * @throws SQLException
     */
    public static void updateUserLastSeen(ProxiedPlayer player) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();

        @SuppressWarnings("deprecation")
        String ip = player.getAddress().getAddress().getHostAddress();

        String query = "UPDATE `" + MySQL.users_table + "` SET `last_seen` = CURRENT_TIMESTAMP, `last_ip` = ? WHERE `"
                + MySQL.users_table + "`.`uuid` = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, ip);
            statement.setString(2, player.getUniqueId().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
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
     * Reset the user token
     * 
     * @param uuid
     * @throws SQLException
     */
    public static void ResetUserToken(UUID uuid) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String query = "UPDATE `" + MySQL.users_table + "` SET `token` = ? WHERE `" + MySQL.users_table
                + "`.`uuid` = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, generateUserToken());
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    /**
     * Get the user token
     * 
     * @param uuid UUID
     * 
     * @return Token
     * @throws SQLException
     */
    public static String getUserToken(UUID uuid) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String query = "SELECT `token` FROM `" + MySQL.users_table + "` WHERE `uuid` = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("token");
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }
    /**
     * Save the user brand name
     * 
     * @param uuid UUID
     * @param brandName Brand name
     * @throws SQLException
     */
    public static void saveBrandName(UUID uuid, String brandName) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String query = "UPDATE `" + MySQL.users_table + "` SET `brand_name` = ? WHERE `" + MySQL.users_table + "`.`uuid` = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, brandName);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }

        String query2 = "INSERT INTO `" + MySQL.brands_table + "` (uuid, value) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query2)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, brandName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Get the user brand name
     * 
     * @param uuid UUID
     * 
     * @return Brand name
     */
    public static String getUserBrandName(UUID uuid) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String query = "SELECT `brand_name` FROM `" + MySQL.users_table + "` WHERE `uuid` = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("brand_name");
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    /**
     * Save user version name
     * 
     * @param uuid UUID
     * @param versionName Version name
     * 
     * @throws SQLException
     */
    public static void saveVersionName(UUID uuid, String versionName) throws SQLException {
        MySQL db = new MySQL();
        Connection conn = db.getConnection();
        String query = "UPDATE `" + MySQL.users_table + "` SET `version_name` = ? WHERE `" + MySQL.users_table + "`.`uuid` = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, versionName);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }

        String query2 = "INSERT INTO `" + MySQL.versions_table + "` (uuid, value) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query2)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, versionName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
}
