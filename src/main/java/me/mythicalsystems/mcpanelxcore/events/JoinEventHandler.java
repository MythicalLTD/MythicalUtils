package me.mythicalsystems.mcpanelxcore.events;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinEventHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        String playerName = e.getPlayer().getName();
        String joinMessage = McPanelX_Core.config.getString("Messages.JoinMessage");
        joinMessage = joinMessage.replace("%player%", playerName);
        e.setJoinMessage(joinMessage);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        String playerName = e.getPlayer().getName();
        String quitMessage = McPanelX_Core.config.getString("Messages.LeaveMessage");
        quitMessage = quitMessage.replace("%player%", playerName);
        e.setQuitMessage(quitMessage);
    }
}
