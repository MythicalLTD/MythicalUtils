package xyz.mythicalsystems.mythicalsystemsutils.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import xyz.mythicalsystems.mythicalsystemsutils.MythicalSystemsUtils;

public class AntiCheat implements Listener {

    /**
     * Kick player if they are flying and not allowed to fly
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        boolean isFlyCheckEnabled = MythicalSystemsUtils.config.getBoolean("AntiCheat.AntiFly");
        if (isFlyCheckEnabled == true) {
            if (player.isFlying() && !player.getAllowFlight()) {
                player.kickPlayer(MythicalSystemsUtils.colorize(MythicalSystemsUtils.config.getString("Messages.KickFly")));
            }
        }
    }

    public void onPlayerLogin(PlayerLoginEvent e) {
        
    }
}
