package xyz.mythicalsystems.McPanelX.commands;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import xyz.mythicalsystems.McPanelX.McPanelX;
import xyz.mythicalsystems.McPanelX.MinecraftPlugin;
import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Link.Account;
import xyz.mythicalsystems.McPanelX.src.Link.Discord;
import xyz.mythicalsystems.McPanelX.src.Messages.Messages;
import xyz.mythicalsystems.McPanelX.src.Translators.ChatTranslator;

public class McPanelXCommand extends Command implements TabExecutor {

    public McPanelXCommand() {
        super("panel", null, "mcpanelx", "mcpanel", "mpx");
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TextComponent(
                    ChatTranslator.Translate(Messages.getMessage().getString("Global.InvalidCommand"))));
            return;
        }
        String subCommand = args[0];

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            switch (subCommand) {
                case "login":
                    if (player.hasPermission("mcpanelx.login")) {
                        player.sendMessage(new TextComponent(
                                ChatTranslator.Translate(Messages.getMessage().getString("Login.Message")
                                        .replace("{token}", Account.getUserInfo(player.getUniqueId(), "token")))));
                    } else {
                        player.sendMessage(new TextComponent(
                                ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
                    }
                    return;
                case "token":
                    if (args.length == 1) {
                        player.sendMessage(new TextComponent(
                                ChatTranslator.Translate(Messages.getMessage().getString("Global.InvalidCommand"))));
                        return;
                    }
                    String tokenSubCommand = args[1];
                    switch (tokenSubCommand) {
                        case "reset":
                            if (player.hasPermission("mcpanelx.token.reset")) {
                                Account.updateUser(player.getUniqueId(), "token", Account.generateUserToken());
                                player.sendMessage(new TextComponent(
                                        ChatTranslator.Translate(Messages.getMessage().getString("Token.TokenReset"))));
                            } else {
                                player.sendMessage(new TextComponent(
                                        ChatTranslator
                                                .Translate(Messages.getMessage().getString("Global.NoPermission"))));
                            }
                            return;
                        case "show":
                            if (player.hasPermission("mcpanelx.token.show")) {
                                player.sendMessage(new TextComponent(ChatTranslator.Translate(
                                        Messages.getMessage().getString("Token.TokenShow").replace("{token}",
                                                Account.getUserInfo(player.getUniqueId(), "token")))));
                            } else {
                                player.sendMessage(new TextComponent(
                                        ChatTranslator
                                                .Translate(Messages.getMessage().getString("Global.NoPermission"))));
                            }
                            return;
                        default:
                            player.sendMessage(new TextComponent(
                                    ChatTranslator
                                            .Translate(Messages.getMessage().getString("Global.InvalidCommand"))));
                            return;
                    }
                    case "discord":
                    if (args.length == 1) {
                        player.sendMessage(new TextComponent(
                                ChatTranslator.Translate(Messages.getMessage().getString("Global.InvalidCommand"))));
                        return;
                    }
                String discordSubCommand = args[1];
                switch (discordSubCommand) {
                    case "link":
                        if (player.hasPermission("mcpanelx.discord.link")) {
                            if (Discord.isAccountLinked(player.getUniqueId())) {
                                player.sendMessage(new TextComponent(
                                        ChatTranslator.Translate(Messages.getMessage().getString("Discord.AlreadyLinked"))));
                                return;
                            }

                            String token = Discord.generatePin();
                            Discord.registerPin(player.getUniqueId(), token);
                            player.sendMessage(new TextComponent(
                                    ChatTranslator.Translate(Messages.getMessage().getString("Discord.Link").replace("{token}", token))));
                        } else {
                            player.sendMessage(new TextComponent(
                                    ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
                        }
                        return;
                    case "unlink":
                        if (player.hasPermission("mcpanelx.discord.unlink")) {
                            if (!Discord.isAccountLinked(player.getUniqueId())) {
                                player.sendMessage(new TextComponent(
                                        ChatTranslator.Translate(Messages.getMessage().getString("Discord.NotLinked"))));
                                return;
                            }
                            Discord.unLinkDiscord(player.getUniqueId());
                            player.sendMessage(new TextComponent(
                                    ChatTranslator.Translate(Messages.getMessage().getString("Discord.Unlink"))));
                        } else {
                            player.sendMessage(new TextComponent(
                                    ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
                        }
                        return;
                    case "invite":
                        if (player.hasPermission("mcpanelx.discord.invite")) {
                            player.sendMessage(new TextComponent(
                                    ChatTranslator.Translate(Messages.getMessage().getString("Discord.Invite"))));
                        } else {
                            player.sendMessage(new TextComponent(
                                    ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
                        }
                        return;
                    default:
                        player.sendMessage(new TextComponent(
                                ChatTranslator
                                        .Translate(Messages.getMessage().getString("Global.InvalidCommand"))));
                        return;
                } 
                case "reload":
                    if (player.hasPermission("mcpanelx.reload")) {
                        McPanelX.reload();
                        sender.sendMessage(new TextComponent(
                                ChatTranslator.Translate(Messages.getMessage().getString("Global.Reloaded"))));
                    } else {
                        player.sendMessage(new TextComponent(
                                ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
                    }
                    return;
                case "help":
                    if (player.hasPermission("mcpanelx.help")) {
                        DisplayHelpMessage(sender);
                        return;
                    } else {
                        player.sendMessage(new TextComponent(
                                ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
                        return;
                    }
                default:
                    sender.sendMessage(new TextComponent(
                            ChatTranslator.Translate(Messages.getMessage().getString("Global.InvalidCommand"))));
                    return;

            }
        } else {
            switch (subCommand) {
                case "reload":
                    McPanelX.reload();
                    sender.sendMessage(new TextComponent(
                            ChatTranslator.Translate(Messages.getMessage().getString("Global.Reloaded"))));
                    return;
                case "help":
                    DisplayHelpMessage(sender);
                    return;
                default:
                    sender.sendMessage(new TextComponent(
                            ChatTranslator.Translate(Messages.getMessage().getString("Global.InvalidCommand"))));
                    return;
            }
        }
    }

    public static void DisplayHelpMessage(CommandSender sender) {
        sender.sendMessage(new TextComponent(ChatTranslator.Translate("&m|----------------------------------------|")));
        sender.sendMessage(new TextComponent(ChatTranslator.Translate("&r")));
        sender.sendMessage(new TextComponent(ChatTranslator.Translate("{maincolor}McPanelX {textcolor}v"
                + MinecraftPlugin.version + " by {secondarycolor}" + MinecraftPlugin.author + "{textcolor}.")));
        sender.sendMessage(new TextComponent(ChatTranslator.Translate("&r")));

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.hasPermission("mcpanelx.help")) {
                sender.sendMessage(
                        new TextComponent(ChatTranslator.Translate("{secondarycolor}/mcpanelx help {textcolor}- "
                                + Messages.getMessage().getString("Help.help"))));
            }
            if (player.hasPermission("mcpanelx.login")) {
                sender.sendMessage(
                        new TextComponent(ChatTranslator.Translate("{secondarycolor}/mcpanelx login {textcolor}- "
                                + Messages.getMessage().getString("Help.login"))));
            }
            if (player.hasPermission("mcpanelx.token.reset")) {
                sender.sendMessage(
                        new TextComponent(ChatTranslator.Translate("{secondarycolor}/mcpanelx token reset {textcolor}- "
                                + Messages.getMessage().getString("Help.tokenReset"))));
            }
            if (player.hasPermission("mcpanelx.token.show")) {
                sender.sendMessage(
                        new TextComponent(ChatTranslator.Translate("{secondarycolor}/mcpanelx token show {textcolor}- "
                                + Messages.getMessage().getString("Help.tokenShow"))));
            }
            if (Config.getSetting().getBoolean("Discord.enabled")) {
                if (player.hasPermission("mcpanelx.discord.link")) {
                    sender.sendMessage(
                            new TextComponent(
                                    ChatTranslator.Translate("{secondarycolor}/mcpanelx discord link {textcolor}- "
                                            + Messages.getMessage().getString("Help.discordLink"))));
                }
                if (player.hasPermission("mcpanelx.discord.unlink")) {
                    sender.sendMessage(
                            new TextComponent(
                                    ChatTranslator.Translate("{secondarycolor}/mcpanelx discord unlink {textcolor}- "
                                            + Messages.getMessage().getString("Help.discordUnlink"))));
                }
                if (player.hasPermission("mcpanelx.discord.invite")) {
                    sender.sendMessage(
                            new TextComponent(
                                    ChatTranslator.Translate("{secondarycolor}/mcpanelx discord invite {textcolor}- "
                                            + Messages.getMessage().getString("Help.discordInvite"))));
                }
            }
            if (player.hasPermission("mcpanelx.reload")) {
                sender.sendMessage(
                        new TextComponent(ChatTranslator.Translate("{secondarycolor}/mcpanelx reload {textcolor}- "
                                + Messages.getMessage().getString("Help.reload"))));
            }

        } else {
            sender.sendMessage(
                    new TextComponent(ChatTranslator.Translate("{secondarycolor}/mcpanelx reload {textcolor}- "
                            + Messages.getMessage().getString("Help.reload"))));
            sender.sendMessage(new TextComponent(ChatTranslator.Translate(
                    "{secondarycolor}/mcpanelx help {textcolor}- " + Messages.getMessage().getString("Help.help"))));
        }
        sender.sendMessage(new TextComponent(ChatTranslator.Translate("&r")));
        sender.sendMessage(new TextComponent(ChatTranslator.Translate("&m|----------------------------------------|")));
        sender.sendMessage(new TextComponent(
                ChatTranslator.Translate("{textcolor}Copyright {maincolor}%year% {textcolor}MythicalSystems LTD")
                        .replace("%year%", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String subCommand = args[0];
            List<String> completions = new ArrayList<>();
            String partialCommand = subCommand.toLowerCase();

            // If the sender is not a player, only allow reload and help
            if (!(sender instanceof ProxiedPlayer)) {
                completions.add("reload");
                completions.add("help");
                return completions;
            }
            if (sender.hasPermission("mcpanelx.help")) {
                if ("help".startsWith(partialCommand)) {
                    completions.add("help");
                }
            }

            if (sender.hasPermission("mcpanelx.reload")) {
                if ("reload".startsWith(partialCommand)) {
                    completions.add("reload");
                }
            }

            if (sender.hasPermission("mcpanelx.login")) {
                if ("login".startsWith(partialCommand)) {
                    completions.add("login");
                }
            }

            if (sender.hasPermission("mcpanelx.token.reset")) {
                if ("token".startsWith(partialCommand)) {
                    completions.add("token");
                }
            }

            if (Config.getSetting().getBoolean("Discord.enabled")) {
                if (sender.hasPermission("mcpanelx.discord") || sender.hasPermission("mcpanelx.discord.link")
                        || sender.hasPermission("mcpanelx.discord.unlink") || sender.hasPermission("mcpanelx.discord.invite")) {
                    if ("discord".startsWith(partialCommand)) {
                        completions.add("discord");
                    }
                }
            }

            return completions;

        } else if (args.length == 2) {
            String subCommand = args[0];
            String tokenSubCommand = args[1];
            List<String> completions = new ArrayList<>();
            String partialCommand = tokenSubCommand.toLowerCase();

            if (subCommand.equals("token")) {
                if (sender.hasPermission("mcpanelx.token.reset")) {
                    if ("reset".startsWith(partialCommand)) {
                        completions.add("reset");
                    }
                }

                if (sender.hasPermission("mcpanelx.token.show")) {
                    if ("show".startsWith(partialCommand)) {
                        completions.add("show");
                    }
                }
            }
            if (Config.getSetting().getBoolean("Discord.enabled")) {
                if (subCommand.equals("discord")) {
                    if (sender.hasPermission("mcpanelx.discord.link")) {
                        if ("link".startsWith(partialCommand)) {
                            completions.add("link");
                        }
                    }

                    if (sender.hasPermission("mcpanelx.discord.unlink")) {
                        if ("unlink".startsWith(partialCommand)) {
                            completions.add("unlink");
                        }
                    }

                    if (sender.hasPermission("mcpanelx.discord.invite")) {
                        if ("invite".startsWith(partialCommand)) {
                            completions.add("invite");
                        }
                    }   
                }
            }

            return completions;
        }
        return ImmutableList.of();
    }
}
