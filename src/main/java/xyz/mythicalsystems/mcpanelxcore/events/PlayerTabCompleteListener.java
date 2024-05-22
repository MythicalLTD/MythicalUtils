package xyz.mythicalsystems.mcpanelxcore.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;

public class PlayerTabCompleteListener implements Listener {
    @EventHandler(priority = 127)
    public void onPlayerTab(TabCompleteEvent event) {
        if (event.isCancelled())
            return;
        if (!(event.getSender() instanceof ProxiedPlayer))
            return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (player.hasPermission("mcpanelx.cmdblocker.bypass"))
            return;
        String command = event.getCursor().split(" ")[0].toLowerCase();
        if (command.length() < 1)
            return;
        command = command.substring(1);
        if (player.hasPermission("mcpanelx.cmdblocker.bypass." + command))
            return;
        if (McPanelX_Core.equalsIgnoreCase(McPanelX_Core.getBlockedCommands(), command))
            event.setCancelled(true);
    }
}
