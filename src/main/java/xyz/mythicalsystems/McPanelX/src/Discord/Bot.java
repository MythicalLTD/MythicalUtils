package xyz.mythicalsystems.McPanelX.src.Discord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;

import xyz.mythicalsystems.McPanelX.McPanelX;
import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Discord.commands.HelpDiscordCommand;
import xyz.mythicalsystems.McPanelX.src.Discord.commands.LinkDiscordCommand;
import xyz.mythicalsystems.McPanelX.src.Discord.commands.UnLinkDiscordCommand;
import xyz.mythicalsystems.McPanelX.src.Discord.event.OnDms;
import xyz.mythicalsystems.McPanelX.src.Messages.Messages;

public class Bot {
    public static DiscordApi bot;
    public Server server;
    public static Bot instance;

    public void start() {
        String str_token = Config.getSetting().getString("Discord.token");
        instance = this;
        new DiscordApiBuilder()
                .setToken(str_token)
                .login()
                .thenAccept(this::onConnectToDiscord)
                .exceptionally(error -> {
                    McPanelX.logger.warn("Bot", "Failed to connect to Discord: " + error.getMessage());
                    return null;
                });
    }

    private void onConnectToDiscord(DiscordApi api) {
        bot = api;
        McPanelX.logger.info("Bot", "Connected to Discord as " + api.getYourself().getDiscriminatedName());
        McPanelX.logger.info("Bot", "Open the following url to invite the bot: " + api.createBotInvite());

        /**
         * Register commands here!
         */
        HelpDiscordCommand.register("help", Messages.getMessage().getString("Bot.Commands.Help.Description"));
        LinkDiscordCommand.register("link", Messages.getMessage().getString("Bot.Commands.Link.Description"));
        UnLinkDiscordCommand.register("unlink", Messages.getMessage().getString("Bot.Commands.UnLink.Description"));

        /**
         * Register events here!
         */
        api.addMessageCreateListener(new OnDms());
    }
    /**
     * This method is used to stop the bot!
     */
    public void stop() {
        if (bot != null) {
            bot.disconnect();
            bot = null;
        }
    }
    /**
     * This method is used to send a message to a user!
     * 
     * @param message
     * @param user_id
     */
    public void sendMessageToUser(EmbedBuilder message, String user_id) {
        bot.getUserById(user_id).thenAccept(user -> {
            user.sendMessage(message).thenAccept(sentMessage -> {
                sentMessage.addReaction("\u2139");
            });
        });
    }
    /**
     * This method is used to send a message to a channel!
     * 
     * @return
     */
    public static Bot getInstance() {
        return instance;
    }
}
