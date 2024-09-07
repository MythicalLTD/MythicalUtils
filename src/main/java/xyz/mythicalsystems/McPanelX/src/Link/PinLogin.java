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
        Bot.getInstance().sendMessageToUser("<@"+Discord.getDiscordId(player.getUniqueId())+">",Discord.getDiscordId(player.getUniqueId()));
        InetSocketAddress socketAddress = (InetSocketAddress) player.getSocketAddress();
        String ipAddress = socketAddress.getAddress().getHostAddress();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = formatter.format(System.currentTimeMillis());
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.Title"))
                .setColor(Color.BLUE)
                .setDescription(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.Description"))
                .setTimestampToNow()
                .addField(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.Name"), player.getDisplayName())
                .addField(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.IP"), ipAddress)
                .addField(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.Date"), date)
                .setFooter(Bot.copyright);
        Bot.getInstance()
                .sendMessageToUser(embedBuilder, Discord.getDiscordId(player.getUniqueId()));

    }
}
