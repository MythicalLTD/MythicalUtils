package xyz.mythicalsystems.mythicalsystemsutils.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import xyz.mythicalsystems.mythicalsystemsutils.MythicalSystemsUtils;

import org.bukkit.command.CommandExecutor;

public class ChatFormatter implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && "reload".equals(args[0])) {

            sender.sendMessage(MythicalSystemsUtils.colorize("&aChatFormatter has been reloaded."));
            return true;
        } else {
            sender.sendMessage(MythicalSystemsUtils.colorize("&aThis command does not exist!"));
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return Collections.singletonList("reload");
        return new ArrayList<>();
    }
}
