package xyz.mythicalsystems.McPanelX.src.Discord.event;

import java.util.UUID;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import xyz.mythicalsystems.McPanelX.src.Link.Account;
import xyz.mythicalsystems.McPanelX.src.Link.Discord;

import java.awt.Color;


public class OnDms implements MessageCreateListener {

    /**
     * This method is called when a user sends a message to the bot!
     */
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageAuthor().isBotUser()) {
            return;
        }

        if (event.getMessageAuthor().isYourself()) {
            return;
        }

        if (event.getMessage().isPrivateMessage()) {
            String message = event.getMessageContent();
            String user_id = event.getMessageAuthor().getIdAsString();

            if (message.startsWith("!login ")) {
                String[] args = message.split(" ");
                if (args.length == 2) {
                    String pin = args[1];
                    if (Discord.doesPinExist(pin)) {
                        try {
                            String uuid = Discord.getUUIDFromDiscord(user_id);
                            if (uuid == null) {
                                EmbedBuilder embedBuilder = new EmbedBuilder()
                                        .setTitle("You are not linked to any account!")
                                        .setColor(Color.RED)
                                        .setDescription("You are not linked to any account!")
                                        .setFooter("MythicalSystems 2024");

                                event.getChannel().sendMessage(embedBuilder).thenAccept(
                                        msg -> {
                                            msg.addReaction("\u274C");
                                        });
                                return;
                            }
                            Account.updateUser(UUID.fromString(uuid), "blocked", "false");
                            Discord.removePin(user_id);
                            EmbedBuilder embedBuilder = new EmbedBuilder()
                                    .setTitle("You have allowed this connection!")
                                    .setColor(Color.GREEN)
                                    .setDescription("You have allowed this connection!")
                                    .setFooter("MythicalSystems 2024");
                            event.getChannel().sendMessage(embedBuilder).thenAccept(msg -> {
                                msg.addReaction("\u2705");
                            });
                        } catch (Exception e) {
                            EmbedBuilder embedBuilder = new EmbedBuilder()
                                    .setTitle("An error occurred!")
                                    .setColor(Color.RED)
                                    .setDescription("An error occurred while trying to link your account!")
                                    .setFooter("MythicalSystems 2024");
                            event.getChannel().sendMessage(embedBuilder).thenAccept(msg -> {
                                msg.addReaction("\u274C");
                            });
                        }
                    } else {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle("Invalid pin!")
                                .setColor(Color.RED)
                                .setDescription("Please provide a valid pin.")
                                .setFooter("Copyright 2024 MythicalSystems!");
                        event.getChannel().sendMessage(embed).thenAccept(msg -> {
                            msg.addReaction("\u274C");
                        });
                    }
                }
            }
        }
    }

}
