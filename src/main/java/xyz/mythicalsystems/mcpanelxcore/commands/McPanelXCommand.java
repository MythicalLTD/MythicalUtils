package xyz.mythicalsystems.mcpanelxcore.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;
import xyz.mythicalsystems.mcpanelxcore.helpers.UserHelper;

public class McPanelXCommand extends Command implements TabExecutor {

    public McPanelXCommand() {
        super("panel", null, "mcpanelx", "mcpanel", "mpx");
    }

    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (args.length == 0) {
                sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Global.InvalidSubCommand")));
                return;
            }
            String subCommand = args[0];
            switch (subCommand) {
                case "reload":
                    if (!sender.hasPermission("mcpanelx.reload")) {
                        // Send message for no permission
                        sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix()+ McPanelX_Core.messages().getString("Global.NoPermission")));
                        return;
                    }
                    if (McPanelX_Core.Reload() == true) {
                        sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Global.Reloaded")));
                        return;
                    } else {
                        sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Global.ReloadFailed")));
                        return;
                    }
                case "help":
                    sender.sendMessage(McPanelX_Core.colorize(""+McPanelX_Core.messages().getString("Global.BasicColor")+"&m|----------------------------------------|"));
                    sender.sendMessage(McPanelX_Core.colorize("&r&l"));
                    sender.sendMessage(McPanelX_Core.colorize(""+McPanelX_Core.messages().getString("Global.MainColor")+"&lMcPanelX "+McPanelX_Core.messages().getString("Global.BasicColor")+"("+McPanelX_Core.messages().getString("Global.MainColor")+""+McPanelX_Core.getVersion()+""+McPanelX_Core.messages().getString("Global.BasicColor")+") -"+McPanelX_Core.messages().getString("Global.SecondaryColor")+"")); 
                    sender.sendMessage(McPanelX_Core.colorize("&r&l"));
                    sender.sendMessage(McPanelX_Core.colorize(""+McPanelX_Core.messages().getString("Global.MainColor")+"/panel help "+McPanelX_Core.messages().getString("Global.BasicColor")+"- "+McPanelX_Core.messages().getString("Global.SecondaryColor")+""+McPanelX_Core.messages().getString("Help.help")));
                    if (sender.hasPermission("mcpanelx.reload")) {
                        sender.sendMessage(McPanelX_Core.colorize(""+McPanelX_Core.messages().getString("Global.MainColor")+"/panel reload "+McPanelX_Core.messages().getString("Global.BasicColor")+"- "+McPanelX_Core.messages().getString("Global.SecondaryColor")+""+McPanelX_Core.messages().getString("Help.reload")));
                    }
                    if (sender.hasPermission("mcpanelx.login")) {
                        sender.sendMessage(McPanelX_Core.colorize(""+McPanelX_Core.messages().getString("Global.MainColor")+"/panel login "+McPanelX_Core.messages().getString("Global.BasicColor")+"- "+McPanelX_Core.messages().getString("Global.SecondaryColor")+""+McPanelX_Core.messages().getString("Help.login")));
                    }
                    if (sender.hasPermission("mcpanelx.token.reset")) {
                        sender.sendMessage(McPanelX_Core.colorize(""+McPanelX_Core.messages().getString("Global.MainColor")+"/panel token reset "+McPanelX_Core.messages().getString("Global.BasicColor")+"- "+McPanelX_Core.messages().getString("Global.SecondaryColor")+""+McPanelX_Core.messages().getString("Help.tokenReset")));
                    }
                    if (sender.hasPermission("mcpanelx.token.show")) {
                        sender.sendMessage(McPanelX_Core.colorize(""+McPanelX_Core.messages().getString("Global.MainColor")+"/panel token show "+McPanelX_Core.messages().getString("Global.BasicColor")+"- "+McPanelX_Core.messages().getString("Global.SecondaryColor")+""+McPanelX_Core.messages().getString("Help.tokenShow")));
                    }
                    if (sender.hasPermission("mcpanelx.alert")) {
                        if (McPanelX_Core.alertSystemEnabled == true) {
                            sender.sendMessage(McPanelX_Core.colorize(""+McPanelX_Core.messages().getString("Global.MainColor")+"/alert "+McPanelX_Core.messages().getString("Global.BasicColor")+"- "+McPanelX_Core.messages().getString("Global.SecondaryColor")+""+McPanelX_Core.messages().getString("Help.alert")));
                        }
                    }
                    if (McPanelX_Core.cfg().getStringList("InGameConsole.Players").contains(player.getName())) {
                        sender.sendMessage(McPanelX_Core.colorize(""+McPanelX_Core.messages().getString("Global.MainColor")+"/bconsole "+McPanelX_Core.messages().getString("Global.BasicColor")+"- "+McPanelX_Core.messages().getString("Global.SecondaryColor")+""+McPanelX_Core.messages().getString("Help.bconsole")));
                    }

                    sender.sendMessage(McPanelX_Core.colorize("&r&l"));
                    sender.sendMessage(McPanelX_Core.colorize(""+McPanelX_Core.messages().getString("Global.BasicColor")+"&m|----------------------------------------|"));
                    sender.sendMessage(McPanelX_Core.colorize(""+McPanelX_Core.messages().getString("Global.BasicColor")+"Copyright "+McPanelX_Core.messages().getString("Global.MainColor")+"&l%year%"+McPanelX_Core.messages().getString("Global.BasicColor")+" MythicalSystems LTD!").replace("%year%", String.valueOf(Calendar.getInstance().get(Calendar.YEAR))));
                    break;
                case "login":
                    if (!sender.hasPermission("mcpanelx.login")) {
                        // Send message for no permission
                        sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix()+ McPanelX_Core.messages().getString("Global.NoPermission")));
                        return;
                    }
                    try {
                        String token = UserHelper.getUserToken(player.getUniqueId());
                        sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Panel.AccountLoginIs").replace("{link}", McPanelX_Core.cfg().getString("Panel.panel_url") + "/auth/login?token=" + token)));
                        return;
                    } catch (SQLException e) {
                        sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Global.Error")));
                        McPanelX_Core.getInstance().getLogger().info("Failed to get the user token: " + e);
                        return;
                    }
                case "token":
                    if (args.length == 1) {
                        sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Global.InvalidSubCommand")));
                        return;
                    }
                    String tokenSubCommand = args[1];
                    switch (tokenSubCommand) {
                        case "reset":
                            if (!sender.hasPermission("mcpanelx.token.reset")) {
                                // Send message for no permission
                                sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix()+ McPanelX_Core.messages().getString("Global.NoPermission")));
                                return;
                            }
                            try {
                                UserHelper.ResetUserToken(player.getUniqueId());
                                sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Panel.AccountTokenRest")));
                                return;
                            } catch (SQLException e) {
                                sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Global.Error")));
                                McPanelX_Core.getInstance().getLogger().info("Failed to reset user token: " + e);
                                return;
                            }
                        case "show":
                            if (!sender.hasPermission("mcpanelx.token.show")) {
                                // Send message for no permission
                                sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix()+ McPanelX_Core.messages().getString("Global.NoPermission")));
                                return;
                            }
                            try {
                                String token = UserHelper.getUserToken(player.getUniqueId());
                                if (token == null) {
                                    sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Global.Error")));
                                    return;
                                } else {
                                    sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Panel.AccountTokenIs").replace("{token}", token)));
                                    return;
                                }
                            } catch (SQLException e) {
                                sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Global.Error")));
                                McPanelX_Core.getInstance().getLogger().info("Failed to get user token: " + e);
                                return;
                            }
                        default:
                            sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Global.InvalidSubCommand")));
                            break;
                    }
                    break;
                default:
                    sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix() + McPanelX_Core.messages().getString("Global.InvalidSubCommand")));
                    break;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {

            String subCommand = args[0];
            List<String> completions = new ArrayList<>();
            String partialCommand = subCommand.toLowerCase();

            if (sender.hasPermission("mcpanelx.reload")) {
                if ("reload".startsWith(partialCommand)) {
                    completions.add("reload");
                }
            }

            if ("help".startsWith(partialCommand)) {
                completions.add("help");
            }

            if (sender.hasPermission("mcpanelx.login")) {
                if ("login".startsWith(partialCommand)) {
                    completions.add("login");
                }
            }

            if (sender.hasPermission("mcpanelx.token.reset")) {
                if ("token".startsWith(partialCommand)) {
                    completions.add("token");
                }
            }

            if (sender.hasPermission("mcpanelx.token.show")) {
                if ("token".startsWith(partialCommand)) {
                    completions.add("token");
                }
            }


            return completions;

        } else if (args.length == 2) {
            String subCommand = args[0];
            String tokenSubCommand = args[1];
            List<String> completions = new ArrayList<>();
            String partialCommand = tokenSubCommand.toLowerCase();

            if (subCommand.equals("token")) {
                if (sender.hasPermission("mcpanelx.token.reset")) {
                    if ("reset".startsWith(partialCommand)) {
                        completions.add("reset");
                    }
                }

                if (sender.hasPermission("mcpanelx.token.show")) {
                    if ("show".startsWith(partialCommand)) {
                        completions.add("show");
                    }
                }
            }

            return completions;
        }
        return ImmutableList.of();
    }
}
