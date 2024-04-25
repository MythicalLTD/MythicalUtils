package me.mythicalsystems.mcpanelxcore.events;

import me.mythicalsystems.mcpanelxcore.database.connection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.SQLException;

public class ChatSaveEvent implements Listener {

    private final connection database;

    public ChatSaveEvent(connection database) {
        this.database = database;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void LogChat(final AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            String message = event.getMessage();
            String playerName = player.getName();
            String playerUUID = player.getUniqueId().toString();
            try {
                if (database != null) {
                    database.insertChatLog(playerName, playerUUID, message);
                } else {
                    Bukkit.getLogger().severe("[McPanelX-Core] Database connection not initialized! Chat logging disabled.");
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("[McPanelX-Core] Failed to insert player chat into database: \n" + e.toString());
            }
        }
    }
}
