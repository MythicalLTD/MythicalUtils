package me.mythicalsystems.mcpanelxcore.commands;

import me.mythicalsystems.mcpanelxcore.Log;
import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Console implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(McPanelX_Core.getPrefix() + McPanelX_Core.config.getString("Messages.OnlyForPlayers"));
            return true;
        }
        Player p = (Player) sender;
        if (!McPanelX_Core.config.getStringList("ConsolePlayers").contains(p.getName())) {
            p.sendMessage(McPanelX_Core.getPrefix()
                    + McPanelX_Core.colorize(McPanelX_Core.config.getString("Messages.NoPermission")));
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(McPanelX_Core.getPrefix()
                    + McPanelX_Core.colorize(McPanelX_Core.config.getString("Messages.ConsoleSyntax")));
            return true;
        }
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < args.length; i++)
            st.append(args[i]).append(" ");
        String command = st.toString().replace("/", "");
        for (String str : McPanelX_Core.config.getStringList("LockedConsoleCommands")) {
            if (command.toLowerCase().contains(str.toLowerCase())) {
                p.sendMessage(McPanelX_Core.getPrefix()
                        + McPanelX_Core.colorize(McPanelX_Core.config.getString("Messages.LockedCommand")));
                return true;
            }
        }
        Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), command);
        p.sendMessage(McPanelX_Core.getPrefix() + McPanelX_Core
                .colorize(McPanelX_Core.config.getString("Messages.Complete").replace("%cmd%", command)));
        Log.Send(p, command);
        return false;
    }
}
