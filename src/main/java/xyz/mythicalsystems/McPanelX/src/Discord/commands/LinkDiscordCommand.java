package xyz.mythicalsystems.McPanelX.src.Discord.commands;

import java.util.Arrays;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Discord.Bot;
import xyz.mythicalsystems.McPanelX.src.Link.Discord;
import xyz.mythicalsystems.McPanelX.src.Messages.Messages;

public class LinkDiscordCommand extends Bot {

    public static void register(String name, String description) {
        SlashCommand command = SlashCommand.with(name, description,
                Arrays.asList(
                        SlashCommandOption.create(SlashCommandOptionType.STRING,
                                getMessage("Bot.Commands.Link.Args.Pin.Name"),
                                getMessage("Bot.Commands.Link.Args.Pin.Description"), true)))
                .createForServer(bot, Config.getSetting().getLong("Discord.guild"))
                .join();

        bot.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if (interaction.getCommandName().equals(command.getName())) {
                String pin = interaction.getArgumentByName("pin").get().getStringValue().get();
                if (Discord.doesPinExist(pin)) {
                    String user_id = interaction.getUser().getIdAsString();
                    if (Discord.isAccountLinked(user_id)) {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(getMessage("Bot.Commands.Link.AlreadyLinked.Title"))
                                .setDescription(getMessage("Bot.Commands.Link.AlreadyLinked.Description"))
                                .setColor(java.awt.Color.RED)
                                .setFooter(Bot.copyright);
                        interaction.createImmediateResponder().addEmbed(embed).respond();
                    } else {
                        Discord.registerDiscord(pin, user_id);
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(getMessage("Bot.Commands.Link.Success.Title"))
                                .setDescription(getMessage("Bot.Commands.Link.Success.Description"))
                                .setColor(java.awt.Color.GREEN)
                                .setFooter(Bot.copyright);
                        interaction.createImmediateResponder().addEmbed(embed).respond();
                        Discord.removePin(user_id);
                    }
                } else {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(getMessage("Bot.Commands.Link.InvalidPin.Title"))
                            .setDescription(getMessage("Bot.Commands.Link.InvalidPin.Description"))
                            .setColor(java.awt.Color.RED)
                            .setFooter(Bot.copyright);
                    interaction.createImmediateResponder().addEmbed(embed).respond();
                }
            }
        });
    }

    /**
     * Get a message from the messages.yml file
     */
    private static String getMessage(String text) {
        return Messages.getMessage().getString(text);
    }
}
