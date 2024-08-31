package xyz.mythicalsystems.McPanelX.events;


import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import xyz.mythicalsystems.McPanelX.McPanelX;
import xyz.mythicalsystems.McPanelX.MinecraftPlugin;
import xyz.mythicalsystems.McPanelX.src.Link.Account;
import xyz.mythicalsystems.McPanelX.src.Messages.Messages;
import xyz.mythicalsystems.McPanelX.src.Translators.ChatTranslator;

public class JoinEvent extends PacketListenerAbstract {
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
            ProxiedPlayer player = MinecraftPlugin.getInstance().getProxy().getPlayer(event.getUser().getProfile().getUUID());
            try {
                MinecraftPlugin.getInstance().getProxy().getScheduler().runAsync(MinecraftPlugin.getInstance(), () -> {
                    Account.onJoin(player, player.getPendingConnection().getVersion(), brand);
                });
            } catch (Exception e) {
                McPanelX.logger.error("JoinEvent",
                        "[McPanelX] Error while saving the players client brand name: " + e.getMessage());
                player.disconnect(new TextComponent(ChatTranslator.Translate(Messages.getMessage().getString("Error.ErrorOnJoin"))));
                return;
            }
            

        } catch (Exception e) {
            McPanelX.logger.error("JoinEvent",
                        "[McPanelX] Error while saving the players client brand name: " + e.getMessage());
        }

    }
}
