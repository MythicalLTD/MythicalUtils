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

public class LinkDiscordCommand extends Bot {

    public static void register(String name, String description) {
        SlashCommand command = SlashCommand.with(name, description,
                Arrays.asList(
                        SlashCommandOption.create(SlashCommandOptionType.STRING, "pin",
                                "The pin you see in minecraft!.", true)))
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
                                .setTitle("Your account is already linked!")
                                .setDescription("Your account is already linked!")
                                .setColor(java.awt.Color.RED)
                                .setFooter("MythicalSystems 2024");
                        interaction.createImmediateResponder().addEmbed(embed).respond();
                    } else {
                        Discord.registerDiscord(pin, user_id);
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle("You have linked your account!")
                                .setDescription("You have linked your account!")
                                .setColor(java.awt.Color.GREEN)
                                .setFooter("MythicalSystems 2024");
                        interaction.createImmediateResponder().addEmbed(embed).respond();
                        Discord.removePin(user_id);
                    }
                } else {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("The pin you entered is invalid!")
                            .setDescription("The pin you entered is invalid!")
                            .setColor(java.awt.Color.RED)
                            .setFooter("MythicalSystems 2024");
                    interaction.createImmediateResponder().addEmbed(embed).respond();
                }
            }
        });
    }

}
