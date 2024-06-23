package me.mythicalsystems.mcpanelxcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;

public class AntiCheat implements Listener {

    /**
     * Kick player if they are flying and not allowed to fly
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        boolean isFlyCheckEnabled = McPanelX_Core.config.getBoolean("AntiCheat.AntiFly");
        if (isFlyCheckEnabled == true) {
            if (player.isFlying() && !player.getAllowFlight()) {
                player.kickPlayer(McPanelX_Core.colorize(McPanelX_Core.config.getString("Messages.KickFly")));
            }
        }
    }

    public void onPlayerLogin(PlayerLoginEvent e) {
        
    }
}
