package xyz.mythicalsystems.mythicalsystemsutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.mythicalsystems.mythicalsystemsutils.Log;
import xyz.mythicalsystems.mythicalsystemsutils.MythicalSystemsUtils;

public class Console implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MythicalSystemsUtils.getPrefix() + MythicalSystemsUtils.config.getString("Messages.OnlyForPlayers"));
            return true;
        }
        Player p = (Player) sender;
        if (!MythicalSystemsUtils.config.getStringList("ConsolePlayers").contains(p.getName())) {
            p.sendMessage(MythicalSystemsUtils.getPrefix()
                    + MythicalSystemsUtils.colorize(MythicalSystemsUtils.config.getString("Messages.NoPermission")));
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(MythicalSystemsUtils.getPrefix()
                    + MythicalSystemsUtils.colorize(MythicalSystemsUtils.config.getString("Messages.ConsoleSyntax")));
            return true;
        }
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < args.length; i++)
            st.append(args[i]).append(" ");
        String command = st.toString().replace("/", "");
        for (String str : MythicalSystemsUtils.config.getStringList("LockedConsoleCommands")) {
            if (command.toLowerCase().contains(str.toLowerCase())) {
                p.sendMessage(MythicalSystemsUtils.getPrefix()
                        + MythicalSystemsUtils.colorize(MythicalSystemsUtils.config.getString("Messages.LockedCommand")));
                return true;
            }
        }
        Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), command);
        p.sendMessage(MythicalSystemsUtils.getPrefix() + MythicalSystemsUtils
                .colorize(MythicalSystemsUtils.config.getString("Messages.Complete").replace("%cmd%", command)));
        Log.Send(p, command);
        return false;
    }
}
