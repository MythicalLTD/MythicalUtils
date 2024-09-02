package xyz.mythicalsystems.McPanelX.src.Discord.commands;


import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Discord.Bot;
import xyz.mythicalsystems.McPanelX.src.Link.Discord;


public class UnLinkDiscordCommand extends Bot {

    public static void register(String name, String description) {
        SlashCommand command = SlashCommand.with(name, description)
                .createForServer(bot, Config.getSetting().getLong("Discord.guild"))
                .join();

        bot.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if (interaction.getCommandName().equals(command.getName())) {
                String user_id = interaction.getUser().getIdAsString();
                if (Discord.isAccountLinked(user_id)) {
                    Discord.unLinkDiscord(user_id);
                    interaction.createImmediateResponder().setContent("Your account has been unlinked!").respond();
                } else {
                    interaction.createImmediateResponder().setContent("Your account is not linked!").respond();
                }
            }
        });
    }

}
