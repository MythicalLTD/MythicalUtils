package xyz.mythicalsystems.mcpanelxcore.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;

public class Console extends Command {

    public Console() {
        super("bungeeconsole", null, "bconsole", "bcex", "mcpanelx console");
    }

    @SuppressWarnings({ "static-access", "deprecation" })
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix()
                    + McPanelX_Core.getInstance().messages().getString("Console.OnlyForPlayers")));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;

        if (!McPanelX_Core.getInstance().cfg().getStringList("InGameConsole.Players").contains(p.getName())) {
            p.sendMessage(McPanelX_Core.colorize(
                    McPanelX_Core.getPrefix()
                            + McPanelX_Core.getInstance().messages().getString("Console.NoPermission")));
            return;
        }
        if (args.length == 0) {
            p.sendMessage(McPanelX_Core.colorize(
                    McPanelX_Core.getPrefix()
                            + McPanelX_Core.getInstance().messages().getString("Console.ConsoleSyntax")));
            return;
        }
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < args.length; i++)
            st.append(args[i]).append(" ");
        String command = st.toString().replace("/", "");
        for (String str : McPanelX_Core.getInstance().cfg().getStringList("InGameConsole.LockedCommands")) {
            if (command.toLowerCase().contains(str.toLowerCase())) {
                sender.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix()
                        + McPanelX_Core.getInstance().messages().getString("Console.LockedCommand")));
                return;
            }
        }
        McPanelX_Core.getInstance().getProxy().getPluginManager()
                .dispatchCommand(McPanelX_Core.getInstance().getProxy().getConsole(), command);
        p.sendMessage(McPanelX_Core.colorize(McPanelX_Core.getPrefix()
                + McPanelX_Core.getInstance().messages().getString("Console.Complete").replace("%cmd%", command)));
        return;
    }

}
