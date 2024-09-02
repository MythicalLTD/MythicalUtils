package xyz.mythicalsystems.McPanelX.src.Link;

import java.net.InetSocketAddress;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.McPanelX.src.Config.Config;

public class PinLogin {
    /**
     * This method is called when a user joins the server!
     */
    public static void onJoin(ProxiedPlayer player) {
        if (Config.getSetting().getBoolean("Discord.enabled")) {
            if (!Config.getSetting().getBoolean("Discord.pinlogin")) {
                return;
            }

            if (Discord.isAccountLinked(player.getUniqueId())) {
                String last_ip = Account.getUserInfo(player.getUniqueId(), "last_ip");
                if (last_ip != null) {
                    if (last_ip == "127.0.0.1" || last_ip == "0.0.0.0" || last_ip == "localhost" || last_ip == ":1"
                            || last_ip == "0:0:0:0:0:0:0:1") {
                        return;
                    } else {
                        String current_ip = ((InetSocketAddress) player.getSocketAddress()).getAddress()
                                .getHostAddress();
                        if (last_ip != current_ip) {
                            
                        } else {
                            return;
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        } else {
            return;
        }
    }
}
