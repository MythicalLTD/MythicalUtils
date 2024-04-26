package me.mythicalsystems.mcpanelxcore.events;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import me.mythicalsystems.mcpanelxcore.database.connection;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConsoleSaveCommand implements Listener {

    private final connection database;

    public ConsoleSaveCommand(connection database) {
        this.database = database;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsoleCommand(final ServerCommandEvent event) {
        if (event.getSender() instanceof BlockCommandSender) return;
        final String command = event.getCommand().replace("\\", "\\\\");
        try {
            if (database != null) {
                database.insertCommandLog(McPanelX_Core.config.getString("Panel.console_name"), "null", command);
            } else {
                Bukkit.getLogger().severe("[McPanelX-Core] Database connection not initialized! Chat logging disabled.");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("[McPanelX-Core] Failed to insert player chat into database: \n" + e.toString());
        }
    }

}
