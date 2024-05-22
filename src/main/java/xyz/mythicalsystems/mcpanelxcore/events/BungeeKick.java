package xyz.mythicalsystems.mcpanelxcore.events;

import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;

public class BungeeKick implements Listener {
    
    @SuppressWarnings("deprecation")
    @EventHandler(priority = 127)
    public void onServerKickEvent(ServerKickEvent ev) {
        ServerInfo kickedFrom = null;
        if (ev.getPlayer().getServer() != null) {
            kickedFrom = ev.getPlayer().getServer().getInfo();
        } else if (McPanelX_Core.getInstance().getProxy().getReconnectHandler() != null) {
            kickedFrom = McPanelX_Core.getInstance().getProxy().getReconnectHandler().getServer(ev.getPlayer());
        } else {
            kickedFrom = AbstractReconnectHandler.getForcedHost(ev.getPlayer().getPendingConnection());
            if (kickedFrom == null)
                kickedFrom = ProxyServer.getInstance()
                        .getServerInfo(ev.getPlayer().getPendingConnection().getListener().getDefaultServer());
        }
        ServerInfo kickTo = McPanelX_Core.getInstance().getProxy().getServerInfo(McPanelX_Core.cfg().getString("BungeeKick.ServerName"));
        if (kickedFrom != null && kickedFrom.equals(kickTo))
            return;
        ev.setCancelled(true);
        ev.setCancelServer(kickTo);
        if (McPanelX_Core.cfg().getBoolean("BungeeKick.ShowKickMessage")) {
            String msg = McPanelX_Core.cfg().getString("Messages.KickMessage");
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            String kmsg = ChatColor.stripColor(BaseComponent.toLegacyText(ev.getKickReasonComponent()));
            msg = String.valueOf(msg) + kmsg;
            ev.getPlayer().sendMessage((BaseComponent) new TextComponent(msg));
        }
    }
}
