package xyz.mythicalsystems.mythicalsystemsutils.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.mythicalsystems.mythicalsystemsutils.MythicalSystemsUtils;

public class MythicalSystemsUtilsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            MythicalSystemsUtils.sendMessage(sender,
                    MythicalSystemsUtils.getPrefix() + ChatColor.GRAY + "Invalid sub-command. Use /mythicalsystemsutils help for a list.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                MythicalSystemsUtils.sendMessage(sender,
                        MythicalSystemsUtils.getPrefix() + ChatColor.GRAY + " Available sub-commands:");
                MythicalSystemsUtils.sendMessage(sender, ChatColor.GRAY + "- /mythicalsystemsutils help: Shows this help message.");
                MythicalSystemsUtils.sendMessage(sender, ChatColor.GRAY + "- /mythicalsystemsutils version: Shows plugin version.");
                MythicalSystemsUtils.sendMessage(sender, ChatColor.GRAY + "- /mythicalsystemsutils author: Shows plugin author(s).");
                MythicalSystemsUtils.sendMessage(sender, ChatColor.GRAY + "- /mythicalsystemsutils clearchat: Clears the chat).");
                MythicalSystemsUtils.sendMessage(sender, ChatColor.GRAY + "- /mythicalsystemsutils reload: Reloads the plugin.");
                MythicalSystemsUtils.sendMessage(sender, ChatColor.GRAY + "- /mythicalsystemsutils lockchat: Locks the chat.");

                return true;
            case "version":
                if (!((Player) sender).hasPermission("mythicalsystemsutils.version")) {
                    MythicalSystemsUtils.sendMessage(sender, MythicalSystemsUtils.getPrefix() + " "
                            + MythicalSystemsUtils.colorize(MythicalSystemsUtils.config.getString("Messages.NoPermission")));
                    return true;
                }
                MythicalSystemsUtils.sendMessage(sender,
                        MythicalSystemsUtils.getPrefix() + "Plugin is running on the version: " + MythicalSystemsUtils.getVersion());
                return true;
            case "clearchat":
                if (!((Player) sender).hasPermission("mythicalsystemsutils.clearchat")) {
                    MythicalSystemsUtils.sendMessage(sender, MythicalSystemsUtils.getPrefix() + " "
                            + MythicalSystemsUtils.colorize(MythicalSystemsUtils.config.getString("Messages.NoPermission")));
                    return true;
                }
                for (int i = 0; i < 150; i++) {
                    Bukkit.broadcastMessage("");
                }
                Bukkit.broadcastMessage(MythicalSystemsUtils.getPrefix() + MythicalSystemsUtils.colorize(MythicalSystemsUtils.config
                        .getString("Messages.ClearChat").replace("%mythicalsystemsutils_user%", sender.getName())));
                return true;
            case "reload":
                if (!((Player) sender).hasPermission("mythicalsystemsutils.reload")) {
                    MythicalSystemsUtils.sendMessage(sender, MythicalSystemsUtils.getPrefix() + " "
                            + MythicalSystemsUtils.colorize(MythicalSystemsUtils.config.getString("Messages.NoPermission")));
                    return true;
                }
                MythicalSystemsUtils.sendMessage(sender, MythicalSystemsUtils.getPrefix() + " " + MythicalSystemsUtils
                        .colorize("&7Im am sorry, but this command is not available in this version of the plugin.&r"));
                return true;
            case "lockchat":
                boolean isPlayer = sender instanceof Player;
                try {
                    if (isPlayer && !((Player) sender).hasPermission("mythicalsystemsutils.lockchat")) {
                        MythicalSystemsUtils.sendMessage(sender, MythicalSystemsUtils.getPrefix() + " "
                                + MythicalSystemsUtils.colorize(MythicalSystemsUtils.config.getString("Messages.NoPermission")));
                        return true;
                    }
                    @SuppressWarnings("unused")
                    String lockedBy;

                    if (isPlayer) {
                        lockedBy = sender.getName();
                    } else {
                        lockedBy = MythicalSystemsUtils.config.getString("Panel.console_name");
                    }
                    if (MythicalSystemsUtils.isChatLocked == true) {
                        MythicalSystemsUtils.isChatLocked = false;
                        Bukkit.broadcast(MythicalSystemsUtils.getPrefix() + MythicalSystemsUtils.colorize(MythicalSystemsUtils.config
                                .getString("Messages.ChatLocked").replace("%mythicalsystemsutils_user%", sender.getName())
                                .replace("%mythicalsystemsutils_status%",
                                        MythicalSystemsUtils.config.getString("Messages.ChatLockedStatusUnLocked"))),
                                "mythicalsystemsutils.lockchat");
                        return true;
                    } else {
                        MythicalSystemsUtils.isChatLocked = true;
                        Bukkit.broadcast(MythicalSystemsUtils.getPrefix() + MythicalSystemsUtils.colorize(MythicalSystemsUtils.config
                                .getString("Messages.ChatLocked").replace("%mythicalsystemsutils_user%", sender.getName())
                                .replace("%mythicalsystemsutils_status%",
                                        MythicalSystemsUtils.config.getString("Messages.ChatLockedStatusLocked"))),
                                "mythicalsystemsutils.lockchat");
                        return true;
                    }
                } catch (Exception e) {
                    MythicalSystemsUtils.sendMessage(sender, MythicalSystemsUtils.colorize(
                            MythicalSystemsUtils.getPrefix() + " &cAn error occurred while trying to lock the chat.:&r") + e);
                    return true;
                }
            case "author":
                if (!((Player) sender).hasPermission("mythicalsystemsutils.author")) {
                    MythicalSystemsUtils.sendMessage(sender, MythicalSystemsUtils.getPrefix() + " "
                            + MythicalSystemsUtils.colorize(MythicalSystemsUtils.config.getString("Messages.NoPermission")));
                    return true;
                }
                MythicalSystemsUtils.sendMessage(sender, MythicalSystemsUtils.getPrefix() + " Plugin author(s): NaysKutzu");
                return true;
            default:
                MythicalSystemsUtils.sendMessage(sender,
                        MythicalSystemsUtils.getPrefix() + " Invalid sub-command. Use /mythicalsystemsutils help for a list.");
                return true;
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return Arrays.asList("help", "version", "clearchat", "reload", "lockchat", "author");
        return new ArrayList<>();
    }
}
