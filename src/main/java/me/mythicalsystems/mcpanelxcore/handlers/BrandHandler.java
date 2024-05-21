package me.mythicalsystems.mcpanelxcore.handlers;

import org.bukkit.Bukkit;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;

import me.mythicalsystems.mcpanelxcore.database.connection;

public class BrandHandler extends PacketListenerAbstract {
    private final connection database;

    public BrandHandler(connection database) {
        this.database = database;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.PLUGIN_MESSAGE
                && event.getPacketType() != PacketType.Configuration.Client.PLUGIN_MESSAGE)
            return;

        WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(event);

        String channelName = packet.getChannelName();
        if (!channelName.equalsIgnoreCase("minecraft:brand") && !channelName.equals("MC|Brand"))
            return;

        byte[] data = packet.getData();

        if (data.length > 64 || data.length == 0)
            return;

        byte[] minusLength = new byte[data.length - 1];
        System.arraycopy(data, 1, minusLength, 0, minusLength.length);
        String brand = new String(minusLength).replace(" (Velocity)", "");
        
        try {
            this.database.savePlayerClientName(event.getUser().getUUID(), brand);
        } catch (Exception e) {
            Bukkit.getLogger().info("[McPanelX-Core] Failed to save players client name to the database: " + e.toString());
        }
    }

}
