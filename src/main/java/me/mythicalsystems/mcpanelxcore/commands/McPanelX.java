package me.mythicalsystems.mcpanelxcore.commands;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class McPanelX implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            McPanelX_Core.sendMessage(sender,
                    McPanelX_Core.getPrefix() + ChatColor.GRAY + "Invalid sub-command. Use /mcpanelx help for a list.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                McPanelX_Core.sendMessage(sender,
                        McPanelX_Core.getPrefix() + ChatColor.GRAY + " Available sub-commands:");
                McPanelX_Core.sendMessage(sender, ChatColor.GRAY + "- /mcpanelx help: Shows this help message.");
                McPanelX_Core.sendMessage(sender, ChatColor.GRAY + "- /mcpanelx version: Shows plugin version.");
                McPanelX_Core.sendMessage(sender, ChatColor.GRAY + "- /mcpanelx author: Shows plugin author(s).");
                McPanelX_Core.sendMessage(sender, ChatColor.GRAY + "- /mcpanelx clearchat: Clears the chat).");
                McPanelX_Core.sendMessage(sender, ChatColor.GRAY + "- /mcpanelx reload: Reloads the plugin.");
                McPanelX_Core.sendMessage(sender, ChatColor.GRAY + "- /mcpanelx lockchat: Locks the chat.");

                return true;
            case "version":
                if (!((Player) sender).hasPermission("mcpanelx.version")) {
                    McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix() + " "
                            + McPanelX_Core.colorize(McPanelX_Core.config.getString("Messages.NoPermission")));
                    return true;
                }
                McPanelX_Core.sendMessage(sender,
                        McPanelX_Core.getPrefix() + "Plugin is running on the version: " + McPanelX_Core.getVersion());
                return true;
            case "clearchat":
                if (!((Player) sender).hasPermission("mcpanelx.clearchat")) {
                    McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix() + " "
                            + McPanelX_Core.colorize(McPanelX_Core.config.getString("Messages.NoPermission")));
                    return true;
                }
                for (int i = 0; i < 150; i++) {
                    Bukkit.broadcastMessage("");
                }
                Bukkit.broadcastMessage(McPanelX_Core.getPrefix() + McPanelX_Core.colorize(McPanelX_Core.config
                        .getString("Messages.ClearChat").replace("%mcpanelx_user%", sender.getName())));
                return true;
            case "reload":
                if (!((Player) sender).hasPermission("mcpanelx.reload")) {
                    McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix() + " "
                            + McPanelX_Core.colorize(McPanelX_Core.config.getString("Messages.NoPermission")));
                    return true;
                }
                McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix() + " " + McPanelX_Core
                        .colorize("&7Im am sorry, but this command is not available in this version of the plugin.&r"));
                return true;
            case "lockchat":
                boolean isPlayer = sender instanceof Player;
                try {
                    if (isPlayer && !((Player) sender).hasPermission("mcpanelx.lockchat")) {
                        McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix() + " "
                                + McPanelX_Core.colorize(McPanelX_Core.config.getString("Messages.NoPermission")));
                        return true;
                    }
                    @SuppressWarnings("unused")
                    String lockedBy;

                    if (isPlayer) {
                        lockedBy = sender.getName();
                    } else {
                        lockedBy = McPanelX_Core.config.getString("Panel.console_name");
                    }
                    if (McPanelX_Core.isChatLocked == true) {
                        McPanelX_Core.isChatLocked = false;
                        Bukkit.broadcast(McPanelX_Core.getPrefix() + McPanelX_Core.colorize(McPanelX_Core.config
                                .getString("Messages.ChatLocked").replace("%mcpanelx_user%", sender.getName())
                                .replace("%mcpanelx_status%",
                                        McPanelX_Core.config.getString("Messages.ChatLockedStatusUnLocked"))),
                                "mcpanelx.lockchat");
                        return true;
                    } else {
                        McPanelX_Core.isChatLocked = true;
                        Bukkit.broadcast(McPanelX_Core.getPrefix() + McPanelX_Core.colorize(McPanelX_Core.config
                                .getString("Messages.ChatLocked").replace("%mcpanelx_user%", sender.getName())
                                .replace("%mcpanelx_status%",
                                        McPanelX_Core.config.getString("Messages.ChatLockedStatusLocked"))),
                                "mcpanelx.lockchat");
                        return true;
                    }
                } catch (Exception e) {
                    McPanelX_Core.sendMessage(sender, McPanelX_Core.colorize(
                            McPanelX_Core.getPrefix() + " &cAn error occurred while trying to lock the chat.:&r") + e);
                    return true;
                }
            case "author":
                if (!((Player) sender).hasPermission("mcpanelx.author")) {
                    McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix() + " "
                            + McPanelX_Core.colorize(McPanelX_Core.config.getString("Messages.NoPermission")));
                    return true;
                }
                McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix() + " Plugin author(s): NaysKutzu");
                return true;
            default:
                McPanelX_Core.sendMessage(sender,
                        McPanelX_Core.getPrefix() + " Invalid sub-command. Use /mcpanelx help for a list.");
                return true;
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return Arrays.asList("help", "version", "clearchat", "reload", "lockchat", "author");
        return new ArrayList<>();
    }
}
