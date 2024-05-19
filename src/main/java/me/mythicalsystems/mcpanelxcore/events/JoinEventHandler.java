package me.mythicalsystems.mcpanelxcore.events;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import me.mythicalsystems.mcpanelxcore.database.connection;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.viaversion.viaversion.api.Via;

public class JoinEventHandler implements Listener {
    private final connection database;

    public JoinEventHandler(connection database) {
        this.database = database;
        setupPacketListener();  // Set up packet listener once when the event handler is instantiated
    }

    private void setupPacketListener() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(McPanelX_Core.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.CUSTOM_PAYLOAD) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.CUSTOM_PAYLOAD) {
                    String channel = event.getPacket().getStrings().readSafely(0);
                    if ("MC|Brand".equals(channel) || "minecraft:brand".equals(channel)) {
                        byte[] data = event.getPacket().getByteArrays().readSafely(0);
                        if (data != null && data.length > 0) {
                            String clientBrand = new String(data);
                            event.getPlayer().sendMessage(McPanelX_Core.colorize("Your client branding is: " + clientBrand));
                        } else {
                            Bukkit.getLogger().log(Level.WARNING, "Received MC|Brand packet with no data.");
                        }
                    }
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        String playerName = e.getPlayer().getName();
        String joinMessage = McPanelX_Core.config.getString("Messages.JoinMessage");

        int nProtocolVersion = Via.getAPI().getPlayerVersion(e.getPlayer());
        try {
            database.savePlayerVersion(e.getPlayer().getUniqueId(), nProtocolVersion);
        } catch (SQLException e1) {
            Bukkit.getLogger().info("Failed to save player version to database: " + e1.toString());
        }

        joinMessage = joinMessage.replace("%player%", playerName);
        e.setJoinMessage(McPanelX_Core.colorize(joinMessage));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        String playerName = e.getPlayer().getName();
        String quitMessage = McPanelX_Core.config.getString("Messages.LeaveMessage");
        quitMessage = quitMessage.replace("%player%", playerName);
        e.setQuitMessage(McPanelX_Core.colorize(quitMessage));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        String playerName = e.getEntity().getName();
        String deathMessage = McPanelX_Core.config.getString("Messages.DeathMessage");
        deathMessage = deathMessage.replace("%player%", playerName);
        e.setDeathMessage(McPanelX_Core.colorize(deathMessage));
    }
}
