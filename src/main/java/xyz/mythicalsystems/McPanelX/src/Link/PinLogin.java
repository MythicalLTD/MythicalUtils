package xyz.mythicalsystems.McPanelX.src.Link;

import java.awt.Color;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.List;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Discord.Bot;
import xyz.mythicalsystems.McPanelX.src.Messages.Messages;
import xyz.mythicalsystems.McPanelX.src.Translators.ChatTranslator;

public class PinLogin {
    /**
     * This method is called when a user joins the server!
     */
    @SuppressWarnings("unchecked")
    public static void onJoin(ProxiedPlayer player) {
        if (Config.getSetting().getBoolean("Discord.enabled")) {
            if (!Config.getSetting().getBoolean("Discord.pinlogin")) {
                return;
            }

            if (Discord.isAccountLinked(player.getUniqueId())) {
                String last_ip = Account.getUserInfo(player.getUniqueId(), "last_ip");

                if (last_ip != null) {
                    String current_ip = ((InetSocketAddress) player.getSocketAddress()).getAddress()
                            .getHostAddress();
                    String isBlockedString = Account.getUserInfo(player.getUniqueId(), "blocked");

                    String str_pin = Discord.generatePin();

                    String finalMsg = "";
                    List<String> loginRequiredMessage = (List<String>) Messages.getMessage()
                            .getList("Discord.LoginRequired");
                    for (String message : loginRequiredMessage) {
                        finalMsg += message.replace("{token}", str_pin) + "\n";
                    }

                    if (!last_ip.equals(current_ip)) {
                        if (isBlockedString.equals("true")) {
                            Account.updateUser(player.getUniqueId(), "discord_pin", str_pin);
                            sendJoinMessage(player);
                            player.disconnect(new TextComponent(ChatTranslator.Translate(finalMsg)));
                            return;
                        } else {
                            Account.updateUser(player.getUniqueId(), "discord_pin", str_pin);
                            Account.updateIP(player);
                            Account.updateUser(player.getUniqueId(), "blocked", "true");
                            sendJoinMessage(player);
                            player.disconnect(new TextComponent(ChatTranslator.Translate(finalMsg)));
                            return;
                        }

                    } else {
                        if (isBlockedString.equals("true")) {
                            Account.updateUser(player.getUniqueId(), "discord_pin", str_pin);
                            sendJoinMessage(player);
                            player.disconnect(new TextComponent(ChatTranslator.Translate(finalMsg)));
                            return;
                        } else {
                            return;
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        } else {
            return;
        }
    }

    /**
     * This method is called when a user joins the server!
     *
     * @param player
     */
    public static void sendJoinMessage(ProxiedPlayer player) {
        InetSocketAddress socketAddress = (InetSocketAddress) player.getSocketAddress();
        String ipAddress = socketAddress.getAddress().getHostAddress();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = formatter.format(System.currentTimeMillis());
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("New login request!")
                .setColor(Color.BLUE)
                .setDescription("There is a new login request on your account!")
                .setTimestampToNow()
                .addField("Nickname", player.getDisplayName())
                .addField("IP Address", ipAddress)
                .addField("Date", date)
                .setFooter("MythicalSystems 2024");
        Bot.getInstance()
                .sendMessageToUser(embedBuilder, Discord.getDiscordId(player.getUniqueId()));

    }
}
