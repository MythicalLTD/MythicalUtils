package me.mythicalsystems.mcpanelxcore.commands;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class ChatFormater implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && "reload".equals(args[0])) {

            sender.sendMessage(McPanelX_Core.colorize("&aChatFormater has been reloaded."));
            return true;
        } else {
            sender.sendMessage(McPanelX_Core.colorize("&aThis command does not exist!"));
        }
        return false;
    }
    
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return Collections.singletonList("reload");
        return new ArrayList<>();
    }
}
