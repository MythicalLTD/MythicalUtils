package me.mythicalsystems.mcpanelxcore.events;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinEventHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        String playerName = e.getPlayer().getName();
        String joinMessage = McPanelX_Core.config.getString("Messages.JoinMessage");
        joinMessage = joinMessage.replace("%player%", playerName);
        e.setJoinMessage(McPanelX_Core.colorize(joinMessage));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        String playerName = e.getPlayer().getName();
        String quitMessage = McPanelX_Core.config.getString("Messages.LeaveMessage");
        quitMessage = quitMessage.replace("%player%", playerName);
        e.setQuitMessage(McPanelX_Core.colorize(quitMessage));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        String playerName = e.getEntity().getName();
        String deathMessage = McPanelX_Core.config.getString("Messages.DeathMessage");
        deathMessage = deathMessage.replace("%player%", playerName);
        e.setDeathMessage(McPanelX_Core.colorize(deathMessage));
    }
}
