package xyz.mythicalsystems.mcpanelxcore.events;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;

public class PlayerChatListener implements Listener {

    @EventHandler(priority = 127)
    public void onPlayerChat(ChatEvent event) {
        if (event.isCancelled())
            return;
        if (!(event.getSender() instanceof ProxiedPlayer))
            return;
        if (!event.isCommand())
            return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (player.hasPermission("mcpanelx.cmdblocker.bypass"))
            return;
        String command = event.getMessage().split(" ")[0].toLowerCase();
        if (command.length() < 1)
            return;
        command = command.substring(1);
        if (player.hasPermission("mcpanelx.cmdblocker.bypass." + command))
            return;
        if (McPanelX_Core.equalsIgnoreCase(McPanelX_Core.getBlockedCommands(), command)) {
            event.setCancelled(true);
            for (String message : McPanelX_Core.getBlockedCommandMessage())
                player.sendMessage(McPanelX_Core.transformString(message.replace("{command}", command)));
            for (ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                if (online.hasPermission("mcpanelx.cmdblocker.notify"))
                    for (String message2 : McPanelX_Core.getBlockedCommandMessageAdmin())
                        online.sendMessage(
                                McPanelX_Core.transformString(message2.replace("{player}", player.getName())));
            }
        }
    }
}
