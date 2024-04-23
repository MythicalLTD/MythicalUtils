package me.mythicalsystems.mcpanelxcore.commands;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Version implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("version")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("mcpanelx.version")) {
                    sender.sendMessage(ChatColor.GRAY +"You are running McPanelX version: "+ChatColor.RED+McPanelX_Core.getVersion()+ChatColor.GRAY+"!");
                    return true;
                } else {
                    sender.sendMessage(McPanelX_Core.getPrefix()+ " "+ McPanelX_Core.config.getString("Messages.NoPermission"));
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.GRAY +"You are running McPanelX version: "+ChatColor.RED+McPanelX_Core.getVersion()+ChatColor.GRAY+"!");
                return true;
            }
        }
        return true;
    }
}
