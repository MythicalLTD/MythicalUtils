package xyz.mythicalsystems.mcpanelxcore.events;

import java.sql.SQLException;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;
import xyz.mythicalsystems.mcpanelxcore.helpers.LoggerHelper;

public class LoggerEvent implements Listener {

    @EventHandler(priority = 127)
    public void onChatEvent(ChatEvent event) {
        if (event.isCancelled())
            return;

        Connection sender = event.getSender();
        if (!(sender instanceof ProxiedPlayer))
            return;
        ProxiedPlayer player = (ProxiedPlayer) sender;
        for (String ignoredCommand : McPanelX_Core.cfg().getStringList("Logger.ignore")) {
            if (event.getMessage().startsWith(ignoredCommand)) {
                return;
            }
        }
        try {
            if (event.isCommand()) {
                if (event.getMessage().startsWith("/login")
                        || event.getMessage().startsWith("/register")
                        || event.getMessage().startsWith("/l")
                        || event.getMessage().startsWith("/changepwd")
                        || event.getMessage().startsWith("/changepassword"))
                    return;
                if (McPanelX_Core.cfg().getBoolean("Logger.commands") == true) {
                    LoggerHelper.logCommand(player, event.getMessage());
                }

            } else {
                if (McPanelX_Core.cfg().getBoolean("Logger.chat") == true) {
                    LoggerHelper.logChatMessage(player, event.getMessage());
                }
            }
        } catch (SQLException e) {
            McPanelX_Core.getInstance().getLogger().severe("An error occurred while trying to log a chat message." + e);
            event.setCancelled(true);
            player.sendMessage(new TextComponent(McPanelX_Core.messages().getString("ChatEvent.FailedToSendMessage")));
        }
    }

}
