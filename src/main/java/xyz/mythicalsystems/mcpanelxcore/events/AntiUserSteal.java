package xyz.mythicalsystems.mcpanelxcore.events;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.event.EventHandler;
import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;
import net.md_5.bungee.api.plugin.Listener;


public class AntiUserSteal implements Listener {
    @EventHandler(priority = 127)
    public void onPlayerLogin(PreLoginEvent pr) {
        for (ProxiedPlayer player : McPanelX_Core.getInstance().getProxy().getPlayers()) {
            if (player.getName().equalsIgnoreCase(pr.getConnection().getName())) {
                pr.setCancelReason(new TextComponent(McPanelX_Core.colorize(McPanelX_Core.getPrefix()
                        + McPanelX_Core.messages().getString("AntiUserSteal.KickMessage"))));
                pr.setCancelled(true);
            }
            String V1 = player.getName().toLowerCase();
            String V2 = pr.getConnection().getName().toLowerCase();
            if (V1.contains(V2) || V2.contains(V1)) {
                pr.setCancelReason(new TextComponent(McPanelX_Core.colorize(McPanelX_Core.getPrefix()
                        + McPanelX_Core.messages().getString("AntiUserSteal.KickMessage"))));
                pr.setCancelled(true);
            }
            pr.setCancelled(false);
        }
    }
}
