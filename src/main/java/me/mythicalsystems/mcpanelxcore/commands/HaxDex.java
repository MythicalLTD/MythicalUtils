package me.mythicalsystems.mcpanelxcore.commands;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HaxDex implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix() + ChatColor.GRAY + " Oh no haxdex dangerous :)");
        McPanelX_Core.sendMessage(sender, McPanelX_Core.getPrefix() + ChatColor.GRAY + " He hack you ;)");
        return true;
    }
}
