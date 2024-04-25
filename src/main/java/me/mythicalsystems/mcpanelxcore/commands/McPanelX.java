package me.mythicalsystems.mcpanelxcore.commands;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class McPanelX implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            McPanelX_Core.sendMessage(sender,McPanelX_Core.getPrefix() + ChatColor.GRAY + " Invalid sub-command. Use /mcpanelx help for a list.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                McPanelX_Core.sendMessage(sender,McPanelX_Core.getPrefix() + ChatColor.GRAY + " Available sub-commands:" );
                McPanelX_Core.sendMessage(sender, ChatColor.GRAY + "- /mcpanelx help: Shows this help message.");
                McPanelX_Core.sendMessage(sender,ChatColor.GRAY + "- /mcpanelx version: Shows plugin version.");
                McPanelX_Core.sendMessage(sender,ChatColor.GRAY +  "- /mcpanelx author: Shows plugin author(s).");
                return true;
            case "version":
                if (!((Player) sender).hasPermission("mcpanelx.version")) {
                    McPanelX_Core.sendMessage(sender,McPanelX_Core.getPrefix() + " " + McPanelX_Core.config.getString("Messages.NoPermission"));
                    return true;
                }
                McPanelX_Core.sendMessage(sender,McPanelX_Core.getPrefix()+"Plugin is running on the version: "+McPanelX_Core.getVersion());
                return true;
            case "author":
                if (!((Player) sender).hasPermission("mcpanelx.author")) {
                    McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix() + " " + McPanelX_Core.config.getString("Messages.NoPermission"));
                    return true;
                }
                McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix() + " Plugin author(s): NaysKutzu");
                return true;
            default:
                McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix()  + " Invalid sub-command. Use /mcpanelx help for a list.");
                return true;
        }
    }
}
