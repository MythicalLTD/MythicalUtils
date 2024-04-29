package me.mythicalsystems.mcpanelxcore.events;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import me.mythicalsystems.mcpanelxcore.database.connection;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayTime implements Listener {
    private final connection database;

    public PlayTime(connection database) {
        this.database = database;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        try {
            database.setPlayTimeOnline(playerUUID);

            // Check if player data exists (implement data loading from storage here)
            long playtime = database.getPlayTimeSeconds(playerUUID);
            Bukkit.getLogger().info(player.getName() + " has a playtime of " + playtime + " seconds");

            // No need to store join time, calculate on quit
        } catch (SQLException e) {
            Bukkit.getLogger().info("[McPanelX] Failed to interact with database! \n" + e.toString());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        try {
            database.setPlayTimeOffline(playerUUID);

            long joinTime = database.getLastJoinTime(playerUUID);
            long currentTime = System.currentTimeMillis();
            long playtime = currentTime - joinTime;

            // Convert milliseconds to seconds (divide by 1000)
            long playtimeInSeconds = playtime / 1000;

            Bukkit.getLogger().info("Your playtime on this server is: " + playtimeInSeconds + " seconds");
            database.setPlayTimeSeconds(playerUUID, playtimeInSeconds);
        } catch (SQLException e) {
            Bukkit.getLogger().info("[McPanelX] Failed to interact with database! \n" + e.toString());
        }
    }
}
