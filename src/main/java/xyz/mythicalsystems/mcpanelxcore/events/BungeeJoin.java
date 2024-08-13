package xyz.mythicalsystems.mcpanelxcore.events;

import java.sql.SQLException;
import java.util.UUID;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;
import xyz.mythicalsystems.mcpanelxcore.helpers.UserHelper;

public class BungeeJoin implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler(priority = 127)
    public void onPlayerJoinEvent(PostLoginEvent ev) {
        ProxiedPlayer player = ev.getPlayer();
        UUID uuid = player.getUniqueId();
        try {
            if (UserHelper.isUserValid(uuid) == true) {
                UserHelper.markUserAsOnline(uuid);
                UserHelper.updateUserLastSeen(player);
            } else {
                try {
                    UserHelper.CreateUser(player.getName(), uuid,
                            player.getAddress().getAddress().getHostAddress());
                    player.disconnect(new TextComponent(
                            "Your account was created successfully. Please rejoin the server in order to play!"));
                } catch (SQLException e) {
                    player.disconnect(new TextComponent(
                            "An error occurred while trying to validate your account. Please try again later."));
                    McPanelX_Core.getInstance().getLogger()
                            .severe("An error occurred while trying to validate a user account." + e);
                }
                player.disconnect(new TextComponent("Your account is not valid. Please contact an administrator."));
            }
        } catch (SQLException e) {
            player.disconnect(new TextComponent(
                    "An error occurred while trying to validate your account. Please try again later."));
        }
    }

    @EventHandler(priority = 127)
    public void onPlayerQuitEvent(ServerDisconnectEvent sve) {
        ProxiedPlayer player = sve.getPlayer();
        UUID uuid = player.getUniqueId();
        try {
            UserHelper.markUserAsOffline(uuid);
        } catch (SQLException e) {
            McPanelX_Core.getInstance().getLogger().severe("An error occurred while trying to update a user account." + e);
        }
    }
}
