package xyz.mythicalsystems.McPanelX.src.Discord.commands;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Discord.Bot;
import xyz.mythicalsystems.McPanelX.src.Messages.Messages;

public class HelpDiscordCommand extends Bot {

    public static void register(String name, String description) {
        SlashCommand command = SlashCommand.with(name, description)
                .createForServer(bot, Config.getSetting().getLong("Discord.guild"))
                .join();

        bot.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if (interaction.getCommandName().equals(command.getName())) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(Messages.getMessage().getString("Bot.Commands.Help.Embed.Title"))
                        .setDescription(Messages.getMessage().getString("Bot.Commands.Help.Embed.Description"))
                        .addField("`/help`", Messages.getMessage().getString("Bot.Commands.Help.Description"), true)
                        .addField("`/link`", Messages.getMessage().getString("Bot.Commands.Link.Description"), true)
                        .addField("`/unlink`", Messages.getMessage().getString("Bot.Commands.Unlink.Description"), true)
                        .setColor(Color.BLUE)
                        .setTimestampToNow()
                        .setFooter(Bot.copyright);

                interaction.createImmediateResponder().addEmbed(embed).respond();
            }
        });
    }
}
