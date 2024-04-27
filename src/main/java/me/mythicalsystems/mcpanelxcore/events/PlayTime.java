package me.mythicalsystems.mcpanelxcore.events;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayTime implements Listener {
    private final Map<UUID, Long> joinTimes; // Stores player join timestamps
    public PlayTime() {
        this.joinTimes = new HashMap<>();
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Check if player data exists (implement data loading from storage here)
        if (!joinTimes.containsKey(playerUUID)) {
            joinTimes.put(playerUUID, System.currentTimeMillis()); // Store join time
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (joinTimes.containsKey(playerUUID)) {
            long joinTime = joinTimes.get(playerUUID);
            long currentTime = System.currentTimeMillis();
            long playtime = currentTime - joinTime;
            long playtimeInSeconds = playtime / 1000;
            Bukkit.getLogger().info("Your playtime on this server is: " + playtimeInSeconds + " seconds");
            joinTimes.remove(playerUUID);
        }
    }

}
