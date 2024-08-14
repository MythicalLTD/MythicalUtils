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
import xyz.mythicalsystems.mcpanelxcore.helpers.ProtocolVersionTranslator;
import xyz.mythicalsystems.mcpanelxcore.helpers.UserHelper;

@SuppressWarnings("unused")
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
                UserHelper.saveVersionName(uuid, ProtocolVersionTranslator
                        .translateProtocolToString(player.getPendingConnection().getVersion()));
            } else {
                try {
                    UserHelper.CreateUser(player.getName(), uuid,
                            player.getAddress().getAddress().getHostAddress());
                    if (McPanelX_Core.cfg().getBoolean("Panel.kick_new_players") == true) {
                        player.disconnect(new TextComponent(
                                McPanelX_Core.colorize(McPanelX_Core.messages().getString("Panel.AccountCreated"))));
                        return;
                    } else {
                        UserHelper.markUserAsOnline(uuid);
                        UserHelper.updateUserLastSeen(player);
                        UserHelper.saveVersionName(uuid, ProtocolVersionTranslator.translateProtocolToString(player.getPendingConnection().getVersion()));
                    }
                } catch (SQLException e) {
                    player.disconnect(new TextComponent(
                            McPanelX_Core.colorize(McPanelX_Core.messages().getString("Panel.AccountCreatedError"))));
                    McPanelX_Core.getInstance().getLogger()
                            .severe("An error occurred while trying to validate a user account." + e);
                    return;
                }
            }
        } catch (SQLException e) {
            player.disconnect(new TextComponent(
                    McPanelX_Core.colorize(McPanelX_Core.messages().getString("Panel.AccountValidationFailed"))));
        }
    }

    @EventHandler(priority = 127)
    public void onPlayerQuitEvent(ServerDisconnectEvent sve) {
        ProxiedPlayer player = sve.getPlayer();
        UUID uuid = player.getUniqueId();
        try {
            UserHelper.updateUserLastSeen(player);
            UserHelper.markUserAsOffline(uuid);
        } catch (SQLException e) {
            McPanelX_Core.getInstance().getLogger()
                    .severe("An error occurred while trying to update a user account." + e);
        }
    }
}
