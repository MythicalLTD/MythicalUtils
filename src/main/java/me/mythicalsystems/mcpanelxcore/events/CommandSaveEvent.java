package me.mythicalsystems.mcpanelxcore.events;

import me.mythicalsystems.mcpanelxcore.database.connection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.sql.SQLException;

public class CommandSaveEvent implements Listener {

    private final connection database;

    public CommandSaveEvent(connection database) {
        this.database = database;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        if (player != null) {
            String command = event.getMessage().replace("\\", "\\\\");
            String playerName = player.getName();
            String playerUUID = player.getUniqueId().toString();
            try {
                if (database != null) {
                    database.insertCommandLog(playerName, playerUUID, command);
                } else {
                    Bukkit.getLogger().severe("[McPanelX-Core] Database connection not initialized! Command logging disabled.");
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("[McPanelX-Core] Failed to insert player command into database: \n" + e.toString());
            }
        }
    }
}
