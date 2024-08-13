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

public class LoggerEvent implements Listener{

    @EventHandler(priority = 127)
    public void onChatEvent(ChatEvent event) { 
        if (event.isCancelled()) return;
        
        Connection sender = event.getSender();
        if (!(sender instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer)sender;

        try {
            if (event.isCommand()) {
                LoggerHelper.logCommand(player, event.getMessage());
            } else {
                LoggerHelper.logChatMessage(player, event.getMessage());
            }
        } catch (SQLException e) {
            McPanelX_Core.getInstance().getLogger().severe("An error occurred while trying to log a chat message." + e);
            event.setCancelled(true);
            player.sendMessage(new TextComponent("An error occurred while trying to send your chat message, please try again later."));
        }
    }

    
}
