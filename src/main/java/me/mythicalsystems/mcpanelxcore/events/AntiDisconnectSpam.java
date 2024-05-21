package me.mythicalsystems.mcpanelxcore.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class AntiDisconnectSpam implements Listener {
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.getReason().equals("disconnect.spam")) {
            event.setCancelled(true);
            Bukkit.getLogger().info("Cancelled kick for " + event.getPlayer().getName() + " (disconnect.spam)");
        } else if (event.getReason().equals("Kicked for spamming")) {
            event.setCancelled(true);
            Bukkit.getLogger().info("Cancelled kick for " + event.getPlayer().getName() + " (Kicked for spamming)");
        }
    }
}
