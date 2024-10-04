package xyz.mythicalsystems.mythicalsystemsutils.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import xyz.mythicalsystems.mythicalsystemsutils.MythicalSystemsUtils;

public class AntiUserSteal implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            String V1 = e.getPlayer().getName().toLowerCase();
            String Vu = players.getName().toLowerCase();
            if (V1.equals(Vu)) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                        MythicalSystemsUtils.colorize(MythicalSystemsUtils.config.getString("Messages.AntiUserSteal")));
            } else {
                e.allow();
            }
        }
    }
}
