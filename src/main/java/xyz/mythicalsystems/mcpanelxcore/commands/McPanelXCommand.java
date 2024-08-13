package xyz.mythicalsystems.mcpanelxcore.commands;

import java.sql.SQLException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;
import xyz.mythicalsystems.mcpanelxcore.helpers.UserHelper;

public class McPanelXCommand extends Command {

    public McPanelXCommand() {
        super("panel", null, "token", "panellink", "link");
    }

    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            try {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "You must add a subcommand!");
                    player.sendMessage(ChatColor.GRAY + "Available subcommands: reset, login");
                    return;
                }

                if (args[0].equalsIgnoreCase("reset")) {
                    UserHelper.ResetUserToken(player.getUniqueId());
                    player.sendMessage(ChatColor.GRAY + "Your token has been reset.");
                } else if (args[0].equalsIgnoreCase("login")) {
                    String token = UserHelper.getUserToken(player.getUniqueId());
                    player.sendMessage(ChatColor.GRAY + "You can use this token to link your account to the panel.");
                    McPanelX_Core.getInstance();
                    player.sendMessage(ChatColor.GRAY + "Here you can find the link: " + ChatColor.GOLD
                            + McPanelX_Core.cfg().getString("Panel.panel_url") + "/auth/login?token=" + token);
                    player.sendMessage(ChatColor.GRAY + "Never share this token with someone else!");
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid subcommand!");
                }

            } catch (SQLException e) {
                player.sendMessage(
                        ChatColor.RED + "An error occurred while trying to get your token. Please try again later.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }

    }
}
