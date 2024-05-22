package xyz.mythicalsystems.mcpanelxcore.events;

import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class SyntaxBlocker implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(ChatEvent  e) {
        if (e.getMessage().split(" ")[0].contains(":")) {
            e.setCancelled(true);
        }
    }
}
