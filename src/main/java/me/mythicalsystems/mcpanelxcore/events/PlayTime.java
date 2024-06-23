package me.mythicalsystems.mcpanelxcore.events;

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
    private final Map<UUID, Long> joinTimeMap;

    public PlayTime(connection database) {
        this.database = database;
        this.joinTimeMap = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        try {
            database.setPlayerJoinTime(playerUUID);
            database.setPlayTimeOnline(playerUUID);
            joinTimeMap.put(playerUUID, System.currentTimeMillis());
        } catch (SQLException e) {
            Bukkit.getLogger().info("[McPanelX] Failed to interact with database! \n" + e.toString());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        try {
            long joinTime = joinTimeMap.getOrDefault(playerUUID, 0L);
            long playTime = System.currentTimeMillis() - joinTime;
            int finalPlayTime = (int) (playTime / 1000) + database.getPlayTimeSeconds(playerUUID); // Convert
                                                                                                   // milliseconds to
                                                                                                   // seconds

            database.setPlayTimeSeconds(playerUUID, finalPlayTime);
            database.setPlayTimeOffline(playerUUID);
            joinTimeMap.remove(playerUUID);

        } catch (SQLException e) {
            Bukkit.getLogger().info("[McPanelX] Failed to interact with database! \n" + e.toString());
        }
    }
}
