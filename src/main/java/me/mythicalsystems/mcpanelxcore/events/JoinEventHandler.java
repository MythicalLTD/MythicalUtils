package me.mythicalsystems.mcpanelxcore.events;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import me.mythicalsystems.mcpanelxcore.database.connection;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.viaversion.viaversion.api.Via;

public class JoinEventHandler implements Listener {
    private final connection database;

    public JoinEventHandler(connection database) {
        this.database = database;
    }

    @SuppressWarnings("unchecked")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        int nProtocolVersion = Via.getAPI().getPlayerVersion(e.getPlayer());
        try {
            database.savePlayerVersion(e.getPlayer().getUniqueId(), nProtocolVersion);
        } catch (SQLException e1) {
            Bukkit.getLogger().info("Failed to save player version to database: " + e1.toString());
        }

        if (McPanelX_Core.config.getBoolean("CustomMessages.enabled") == true) {
            String playerName = e.getPlayer().getName();
            String joinMessage = McPanelX_Core.config.getString("Messages.JoinMessage");

            joinMessage = joinMessage.replace("%player%", playerName);
            e.setJoinMessage(McPanelX_Core.colorize(joinMessage));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        if (McPanelX_Core.config.getBoolean("CustomMessages.enabled") == true) {
            String playerName = e.getPlayer().getName();
            String quitMessage = McPanelX_Core.config.getString("Messages.LeaveMessage");
            quitMessage = quitMessage.replace("%player%", playerName);
            e.setQuitMessage(McPanelX_Core.colorize(quitMessage));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        if (McPanelX_Core.config.getBoolean("CustomMessages.enabled") == true) {
            String playerName = e.getEntity().getName();
            String deathMessage = McPanelX_Core.config.getString("Messages.DeathMessage");
            deathMessage = deathMessage.replace("%player%", playerName);
            e.setDeathMessage(McPanelX_Core.colorize(deathMessage));
        }
    }
}
