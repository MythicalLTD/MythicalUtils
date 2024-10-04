package xyz.mythicalsystems.mythicalsystemsutils.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

import xyz.mythicalsystems.mythicalsystemsutils.MythicalSystemsUtils;

public class ConsoleCommandBlock implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsoleCommandPreProcess(ServerCommandEvent event) {
        String command = event.getCommand();

        if (MythicalSystemsUtils.config.getList("LockedConsoleCommands") != null) {
            for (String blockedCommand : MythicalSystemsUtils.config.getStringList("LockedConsoleCommands")) {
                if (blockedCommand.equalsIgnoreCase(command)) {
                    Bukkit.getLogger().info("[McPanelX] " + MythicalSystemsUtils.config.getString("Messages.LockedCommand"));
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRconCommand(RemoteServerCommandEvent event) {
        String RCommand = event.getCommand();

        if (MythicalSystemsUtils.config.getList("LockedConsoleCommands") != null) {
            for (String blockedCommand : MythicalSystemsUtils.config.getStringList("LockedConsoleCommands")) {
                if (blockedCommand.equalsIgnoreCase(RCommand)) {
                    event.setCancelled(true);
                    Bukkit.getLogger().info("[McPanelX] " + MythicalSystemsUtils.config.getString("Messages.LockedCommand"));
                    break;
                }
            }
        }
    }

}
