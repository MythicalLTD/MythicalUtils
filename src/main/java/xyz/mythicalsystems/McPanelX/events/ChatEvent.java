package xyz.mythicalsystems.McPanelX.events;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import xyz.mythicalsystems.McPanelX.MinecraftPlugin;
import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Link.Logger;

public class ChatEvent implements Listener {
    @EventHandler(priority = 127)
    public void onChat(net.md_5.bungee.api.event.ChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Connection sender = event.getSender();
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        for (String ignoredCommand : Config.getSetting().getStringList("Logger.ignore")) {
            if (event.getMessage().startsWith(ignoredCommand)) {
                return;
            }
        }
        if (event.isCommand()) {
            if (event.getMessage().startsWith("/login")
                    || event.getMessage().startsWith("/register")
                    || event.getMessage().startsWith("/l")
                    || event.getMessage().startsWith("/changepwd")
                    || event.getMessage().startsWith("/changepassword"))
                return;
            if (Config.getSetting().getBoolean("Logger.commands") == true) {
                MinecraftPlugin.getInstance().getProxy().getScheduler().runAsync(MinecraftPlugin.getInstance(), () -> {
                    Logger.logCommand(player, event.getMessage());
                });
            }

        } else {
            if (Config.getSetting().getBoolean("Logger.chat") == true) {
                MinecraftPlugin.getInstance().getProxy().getScheduler().runAsync(MinecraftPlugin.getInstance(), () -> {
                    Logger.logChat(player, event.getMessage());
                });
            }
        }
    }
}
