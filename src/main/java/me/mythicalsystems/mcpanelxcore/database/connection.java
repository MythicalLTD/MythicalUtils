package me.mythicalsystems.mcpanelxcore.database;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import me.mythicalsystems.mcpanelxcore.handlers.ProtocolVersionTranslator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class connection {
    private Connection connection;

    /**
     * Gets the database connection
     *
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }

        String host = McPanelX_Core.config.getString("Database.host");
        int port = McPanelX_Core.config.getInt("Database.port");
        String database = McPanelX_Core.config.getString("Database.database");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        String user = McPanelX_Core.config.getString("Database.username");
        String password = McPanelX_Core.config.getString("Database.password");

        Connection connection = DriverManager.getConnection(url, user, password);
        this.connection = connection;
        Bukkit.getLogger().info("[MCPanelX-Core] Connected to the MySQL "
                + McPanelX_Core.config.getString("Database.host") + " server!");
        return connection;
    }

    /**
     * Creates the tables and checks if the database is healthy
     *
     *
     * @throws SQLException
     */
    public void initializeDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS `" + McPanelX_Core.config.getString("Database.database")
                + "`.`mcpanelx_core_logs` (`id` INT NOT NULL AUTO_INCREMENT , `uuid` TEXT NOT NULL , `name` TEXT NOT NULL , `server_name` TEXT NOT NULL, `type` ENUM('command','chat') NOT NULL , `value` TEXT NOT NULL , `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
        String sql2 = "CREATE TABLE IF NOT EXISTS `" + McPanelX_Core.config.getString("Database.database")
                + "`.`mcpanelx_core_users` (`id` INT NOT NULL AUTO_INCREMENT , `uuid` TEXT NOT NULL , `name` TEXT NOT NULL , `server_name` TEXT NOT NULL , `online` ENUM('offline','online') NOT NULL , `value` INT NOT NULL , `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`id`)) ENGINE = InnoDB;";
        String sql3 = "CREATE TABLE IF NOT EXISTS `" + McPanelX_Core.config.getString("Database.database")
                + "`.`mcpanelx_core_versions` (`id` INT NOT NULL AUTO_INCREMENT , `uuid` TEXT NOT NULL , `version` TEXT NOT NULL , `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
        String sql4 = "CREATE TABLE IF NOT EXISTS `" + McPanelX_Core.config.getString("Database.database")
                + "`.`mcpanelx_core_brands` (`id` INT NOT NULL AUTO_INCREMENT , `uuid` TEXT NOT NULL , `name` TEXT NOT NULL , `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
        statement.execute(sql);
        statement.execute(sql2);
        statement.execute(sql3);
        statement.execute(sql4);

        statement.close();
    }

    /**
     * Insert a chat log inside the logs database
     *
     * @param uuid     The uuid of the user
     * @param username The username of the user
     * @param value    The value you want to insert (ChatLog)
     *
     * @return void
     * @throws SQLException
     */
    @SuppressWarnings("null")
    public void insertChatLog(String uuid, String username, String value) throws SQLException {
        if (uuid == null || username == null || value == null) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot insert chat log with null values!");
        }
        String sql = "INSERT INTO `mcpanelx_core_logs` (`uuid`, `name`, `server_name`, `type`, `value`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(sql);
        statement.setString(1, username.toString());
        statement.setString(2, uuid.toString());
        statement.setString(3, McPanelX_Core.config.getString("Panel.server_name").toString());
        statement.setString(4, "chat");
        statement.setString(5, value.toString());
        try {
            statement.execute();
        } catch (Exception e) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot insert chat log! " + e.toString());
        }
        statement.close();
    }

    /**
     * Insert a command log inside the log database
     *
     * @param uuid     The uuid of the player
     * @param username The username of the player
     * @param value    The value like for example the command
     *
     * @return void
     * @throws SQLException
     */
    public void insertCommandLog(String uuid, String username, String value) throws SQLException {
        if (uuid == null || username == null || value == null) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot insert chat log with null values!");
        }
        String sql = "INSERT INTO `mcpanelx_core_logs` (`uuid`, `name`, `server_name`, `type`, `value`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, uuid);
        statement.setString(3, McPanelX_Core.config.getString("Panel.server_name"));
        statement.setString(4, "command");
        statement.setString(5, value);
        try {
            statement.execute();
        } catch (Exception e) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot insert command log! " + e.toString());
        }
        statement.close();
    }

    /**
     * Get the player playtime in seconds
     *
     * @param uuid The uuid of the player
     *
     * @return The time in an int value!
     * @throws SQLException
     */
    public int getPlayTimeSeconds(UUID uuid) throws SQLException {
        if (uuid == null) {
            return 0;
        }
        if (doesUserExist(uuid) == true) {
            String sql = "SELECT SUM(value) AS playtime_seconds FROM `mcpanelx_core_users` WHERE `uuid` = ? AND `server_name` = ? LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid.toString());
            statement.setString(2, McPanelX_Core.config.getString("Panel.server_name"));
            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    int nPlayTime = results.getInt("playtime_seconds");
                    return nPlayTime;
                } else {
                    return 0;
                }
            } finally {
                statement.close();
            }
        } else {
            return 0;
        }
    }

    /**
     * Checks if a user is valid (ONJOIN)
     *
     * @param player The player
     *
     * @throws SQLException
     * @return void
     */
    @SuppressWarnings("null")
    public void ValidUser(Player player) throws SQLException {
        if (player == null) {
            Bukkit.getLogger().info("[McPanelX] Failed to find the user due to no uuid given");
        }
        if (doesUserExist(player.getUniqueId()) == true) {
            Bukkit.getLogger()
                    .info("[McPanelX] Player " + player.getName() + " is a valid player inside our database!");
        } else {
            CreatePlayer(player);
        }
    }

    public void CreatePlayer(Player player) throws SQLException {
        String sql = "INSERT INTO `mcpanelx_core_users` (`uuid`, `name`, `server_name`, `online`, `value`) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement statement = getConnection().prepareStatement(sql);
        statement.setString(1, player.getUniqueId().toString());
        statement.setString(2, player.getName());
        statement.setString(3, McPanelX_Core.config.getString("Panel.server_name"));
        statement.setString(4, "online");
        statement.setInt(5, 0);

        try {
            statement.execute();
        } catch (Exception e) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot insert command log! " + e.toString());
        }
        statement.close();
    }

    /**
     * Looks if a user exists inside the database!
     *
     * @param uuid The uuid of the user
     *
     * @return If true then it means that it does if false then it does not!
     *
     * @throws SQLException
     */
    public boolean doesUserExist(UUID uuid) throws SQLException {
        if (uuid == null) {
            Bukkit.getLogger().info("[McPanelX] Failed to find the user due to no uuid given");
            return false;
        }
        String sql = "SELECT * FROM `mcpanelx_core_users` WHERE `uuid` = ? AND `server_name` = ? LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, uuid.toString());
        statement.setString(2, McPanelX_Core.config.getString("Panel.server_name"));

        try (ResultSet results = statement.executeQuery()) {
            return results.next();
        } catch (SQLException ex) {
            Bukkit.getLogger().info("[McPanelX] Failed to get user status " + ex.toString());
        } finally {
            statement.close();
        }
        return false;
    }

    /**
     * Set the player playtime in the database
     *
     * @param uuid    The uuid of the player
     * @param seconds The time in seconds
     *
     * @throws SQLException
     * @return void
     */
    @SuppressWarnings("null")
    public void setPlayTimeSeconds(UUID uuid, long seconds) throws SQLException {
        if (uuid == null) {
            Bukkit.getLogger().info("[McPanelX] Failed to update seconds due to the UUID being null!");
        }
        if (doesUserExist(uuid) == true) {
            String sql = "UPDATE `mcpanelx_core_users` SET `value` = ? WHERE `mcpanelx_core_users`.`uuid` = ? AND `server_name` = ? LIMIT 1";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, longToIntCast(seconds));
            statement.setString(2, uuid.toString());
            statement.setString(3, McPanelX_Core.config.getString("Panel.server_name"));

            try {
                statement.executeUpdate();
            } catch (Exception e) {
                Bukkit.getLogger().info("[McPanelX-Core] Cannot set playtime in the database! " + e.getMessage());
            }
            statement.close();
        }
    }

    /**
     * Set a player as offline
     * 
     * @param uuid The uuid of the player
     *
     * @return void
     */
    @SuppressWarnings("null")
    public void setPlayTimeOffline(UUID uuid) throws SQLException {
        if (uuid == null) {
            Bukkit.getLogger().info("[McPanelX] Failed to update seconds due to the UUID being null!");
        }
        if (doesUserExist(uuid) == true) {
            String sql = "UPDATE `mcpanelx_core_users` SET `online` = 'offline' WHERE `mcpanelx_core_users`.`uuid` = ? AND `server_name` = ?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setString(1, uuid.toString());
            statement.setString(2, McPanelX_Core.config.getString("Panel.server_name"));
            try {
                statement.execute();
            } catch (Exception e) {
                Bukkit.getLogger().info("[McPanelX-Core] Cannot update the database!! " + e.toString());
            }
            statement.close();
        }
    }

    /**
     * Set a player as online
     * 
     * @param uuid The uuid of the player
     *
     * @return void
     */
    @SuppressWarnings("null")
    public void setPlayTimeOnline(UUID uuid) throws SQLException {
        if (uuid == null) {
            Bukkit.getLogger().info("[McPanelX] Failed to update seconds due to the UUID being null!");
        }
        if (doesUserExist(uuid) == true) {
            String sql = "UPDATE `mcpanelx_core_users` SET `online` = 'online' WHERE `mcpanelx_core_users`.`uuid` = ? AND `server_name` = ?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setString(1, uuid.toString());
            statement.setString(2, McPanelX_Core.config.getString("Panel.server_name"));

            try {
                statement.execute();
            } catch (Exception e) {
                Bukkit.getLogger().info("[McPanelX-Core] Cannot update database! " + e.toString());
            }
            statement.close();
        } else {
            ValidUser(McPanelX_Core.getPlayerByUUID(uuid));
        }
    }

    /**
     * Set the player join Time
     *
     * @param uuid The player uuid
     *
     */
    public void setPlayerJoinTime(UUID uuid) throws SQLException {
        if (uuid == null) {
            Bukkit.getLogger().info("[McPanelX] Failed to update join time due to the UUID being null!");
            return;
        }
        if (doesUserExist(uuid)) {
            String sql = "UPDATE `mcpanelx_core_users` SET `date` = NOW() WHERE `uuid` = ? AND `server_name` = ?";
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setString(1, uuid.toString());
            statement.setString(2, McPanelX_Core.config.getString("Panel.server_name"));

            try {
                statement.executeUpdate();
                Bukkit.getLogger().info("[McPanelX] Updated join time for user: " + uuid.toString());
            } catch (SQLException e) {
                Bukkit.getLogger().info("[McPanelX-Core] Cannot update database! " + e.toString());
            } finally {
                statement.close();
            }
        } else {
            ValidUser(McPanelX_Core.getPlayerByUUID(uuid));
        }
    }

    /**
     * Mark the hole server as offline
     * 
     * @throws SQLException
     */
    public void markHoleServerAsOffline() throws SQLException {
        String sql = "UPDATE `mcpanelx_core_users` SET `online` = 'offline' WHERE `server_name` = ?";
        PreparedStatement statement = getConnection().prepareStatement(sql);
        statement.setString(1, McPanelX_Core.config.getString("Panel.server_name"));

        try {
            statement.execute();
        } catch (Exception e) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot update database! " + e.toString());
        }
        statement.close();
    }
    /**
     * Save the player version
     * 
     * @param uuid
     * @param version
     * @throws SQLException
     */
    public void savePlayerVersion(UUID uuid, int protocolVersionNumber) throws SQLException {
        if (uuid == null) {
            Bukkit.getLogger().info("[McPanelX] Failed to update join time due to the UUID being null!");
            return;
        }
        String sql = "INSERT INTO `mcpanelx_core_versions` (`uuid`, `version`) VALUES (?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(sql);
        statement.setString(1, uuid.toString());
        statement.setString(2, ProtocolVersionTranslator.translateProtocolToString(protocolVersionNumber));

        try {
            statement.execute();
        } catch (Exception e) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot update database! " + e.toString());
        }
        statement.close();
    }

    public void savePlayerClientName(UUID uuid, String name) throws SQLException {
        if (uuid == null) {
            Bukkit.getLogger().info("[McPanelX] Failed to update join time due to the UUID being null!");
            return;
        }
        String sql = "INSERT INTO `mcpanelx_core_brands` (`uuid`, `name`) VALUES (?, ?)";
        PreparedStatement statement = getConnection().prepareStatement(sql);
        statement.setString(1, uuid.toString());
        statement.setString(2, name);

        try {
            statement.execute();
        } catch (Exception e) {
            Bukkit.getLogger().info("[McPanelX-Core] Cannot update database! " + e.toString());
        }
        statement.close();
    }

    /**
     * Convert a long to an int
     *
     * @param number The number you want to convert!
     *
     * @return The int
     */
    public int longToIntCast(long number) {
        return (int) number;
    }

    /**
     * Convert an int to a long
     * 
     * @param number The number you want to convert!
     *
     * @return the long
     *
     */
    public long intToLongCast(int number) {
        return (long) number;
    }
}
