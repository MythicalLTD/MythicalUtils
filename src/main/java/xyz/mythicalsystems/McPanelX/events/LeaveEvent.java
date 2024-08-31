package xyz.mythicalsystems.McPanelX.events;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import xyz.mythicalsystems.McPanelX.McPanelX;
import xyz.mythicalsystems.McPanelX.MinecraftPlugin;
import xyz.mythicalsystems.McPanelX.src.Link.Account;


public class LeaveEvent implements Listener {
    
    @EventHandler(priority = 127)
    public void onPlayerLeave(PlayerDisconnectEvent pde) {
        try {
            MinecraftPlugin.getInstance().getProxy().getScheduler().runAsync(MinecraftPlugin.getInstance(), () -> {
                Account.onLeave(pde.getPlayer());
            });
        } catch (Exception e) {
            McPanelX.logger.error("LeaveEvent", "Error while tried to save the player info: " + e.getMessage());
        }
    }
}
