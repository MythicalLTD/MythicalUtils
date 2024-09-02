package xyz.mythicalsystems.McPanelX.src.Discord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;

import xyz.mythicalsystems.McPanelX.McPanelX;
import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Discord.commands.HelpDiscordCommand;
import xyz.mythicalsystems.McPanelX.src.Discord.commands.LinkDiscordCommand;
import xyz.mythicalsystems.McPanelX.src.Discord.commands.UnLinkDiscordCommand;

public class Bot {
    public static DiscordApi bot;
    public Server server;

    public void start() {
        String str_token = Config.getSetting().getString("Discord.token");

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
        HelpDiscordCommand.register("help", "This is the help command!");
        LinkDiscordCommand.register("link", "The command used to link your discord account to your minecraft account!");
        UnLinkDiscordCommand.register("unlink", "The command used to unlink your discord account from your minecraft account!");
    }

    public void stop() {
        if (bot != null) {
            bot.disconnect();
            bot = null;
        }
    }

}
