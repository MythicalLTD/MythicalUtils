package xyz.mythicalsystems.McPanelX.src.Discord.commands;


import java.util.Arrays;

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
                        interaction.createImmediateResponder().setContent("Your account is already linked!").respond();
                    } else {
                        Discord.registerDiscord(pin, user_id);
                        interaction.createImmediateResponder().setContent("Your account has been linked!").respond();
                        Discord.removePin(user_id);
                    }
                } else {
                    interaction.createImmediateResponder().setContent("This pin is not valid in our database!").respond();
                }
            }
        });
    }

}
