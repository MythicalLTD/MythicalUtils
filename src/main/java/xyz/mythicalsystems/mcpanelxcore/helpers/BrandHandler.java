package xyz.mythicalsystems.mcpanelxcore.helpers;

import java.sql.SQLException;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;

import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;

public class BrandHandler extends PacketListenerAbstract {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        try {
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
                UserHelper.saveBrandName(event.getUser().getUUID(), brand);
            } catch (SQLException e) {
                McPanelX_Core.getInstance().getLogger()
                        .severe("[McPanelX] Error while saving the players client brand name: " + e.getMessage());
            }
        } catch (Exception e) {
            McPanelX_Core.getInstance().getLogger()
                    .severe("[McPanelX] Error while getting the players client brand name: " + e.getMessage());
        }

    }

}
