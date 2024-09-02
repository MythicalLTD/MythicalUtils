package xyz.mythicalsystems.McPanelX.src.Discord.commands;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Discord.Bot;


public class HelpDiscordCommand extends Bot {

    public static void register(String name, String description) {
        SlashCommand command = SlashCommand.with(name, description)
                .createForServer(bot, Config.getSetting().getLong("Discord.guild"))
                .join();

        bot.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if (interaction.getCommandName().equals(command.getName())) {
                int currentYear = java.time.Year.now().getValue();
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Help Command")
                        .setDescription("This is the main help command of the bot!")
                        .addField("`/help`", "The help command of the bot!", true)
                        .addField("`/link`", "Link your minecraft account!", true)
                        .setColor(Color.BLUE)
                        .setTimestampToNow()
                        .setFooter("Copyright " + currentYear+" MythicalSystems!");
                
                interaction.createImmediateResponder().addEmbed(embed).respond();
            }
        });
    }
}
