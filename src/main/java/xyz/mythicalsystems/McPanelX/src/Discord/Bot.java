package xyz.mythicalsystems.McPanelX.src.Discord;

import java.time.Duration;

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
    public static String copyright = "Copyright %year% MythicalSystems!";

    public void start() {
        String str_token = Config.getSetting().getString("Discord.token");
        int currentYear = java.time.Year.now().getValue();
        copyright = copyright.replace("%year%", String.valueOf(currentYear));

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
        McPanelX.logger.info("Bot", "Registering commands...");
        McPanelX.logger.info("Bot", "Registering /help command...");
        HelpDiscordCommand.register("help", Messages.getMessage().getString("Bot.Commands.Help.Description"));
        McPanelX.logger.info("Bot", "Registering /link command...");
        LinkDiscordCommand.register("link", Messages.getMessage().getString("Bot.Commands.Link.Description"));
        McPanelX.logger.info("Bot", "Registering /unlink command...");
        UnLinkDiscordCommand.register("unlink", Messages.getMessage().getString("Bot.Commands.Unlink.Description"));
        McPanelX.logger.info("Bot", "Commands registered!");
        /**
         * Register events here!
         */
        McPanelX.logger.info("Bot", "Registering events...");
        McPanelX.logger.info("Bot", "Registering OnDms event...");
        api.addMessageCreateListener(new OnDms());
        McPanelX.logger.info("Bot", "Events registered!");
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
     * This method is used to send a message to a user!
     * 
     * @param message
     * @param user_id
     */
    public void sendMessageToUser(String message, String user_id) {
        bot.getUserById(user_id).thenAccept(user -> {
            user.sendMessage(message).thenAccept(sentMessage -> {
                sentMessage.addReaction("\u2139");
                sentMessage.deleteAfter(Duration.ofSeconds(3));
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
