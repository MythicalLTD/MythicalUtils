package me.mythicalsystems.mcpanelxcore.events;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class ConsoleCommandBlock implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsoleCommandPreProcess(ServerCommandEvent event) {
        String command = event.getCommand();

        if (McPanelX_Core.config.getList("LockedConsoleCommands") != null) {
            for (String blockedCommand : McPanelX_Core.config.getStringList("LockedConsoleCommands")) {
                if (blockedCommand.equalsIgnoreCase(command)) {
                    Bukkit.getLogger().info("[McPanelX] " + McPanelX_Core.config.getString("Messages.LockedCommand"));
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRconCommand(RemoteServerCommandEvent event) {
        String RCommand = event.getCommand();

        if (McPanelX_Core.config.getList("LockedConsoleCommands") != null) {
            for (String blockedCommand : McPanelX_Core.config.getStringList("LockedConsoleCommands")) {
                if (blockedCommand.equalsIgnoreCase(RCommand)) {
                    event.setCancelled(true);
                    Bukkit.getLogger().info("[McPanelX] " + McPanelX_Core.config.getString("Messages.LockedCommand"));
                    break;
                }
            }
        }
    }

}
