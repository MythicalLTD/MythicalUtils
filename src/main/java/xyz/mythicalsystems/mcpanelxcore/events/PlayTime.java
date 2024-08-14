package xyz.mythicalsystems.mcpanelxcore.events;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;
import xyz.mythicalsystems.mcpanelxcore.helpers.UserHelper;
import xyz.mythicalsystems.mcpanelxcore.objects.Player;
import xyz.mythicalsystems.mcpanelxcore.utils.PlayTimeHelper;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayTime implements Listener {

	private Player player;

	@EventHandler(priority = 127)
	public void onPostLoginEvent(PostLoginEvent e) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String lastLogin = formatter.format(new Date());
		
		McPanelX_Core.getInstance().getLogger().info("Player " + e.getPlayer().getName() + " logged in at " + lastLogin);



		PlayTimeHelper.getPlayTimePlayers().add(new Player(e.getPlayer().getUniqueId(), lastLogin));
	}

	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent e) {
		// Get current player from saved data array
		player = null;
		PlayTimeHelper.getPlayTimePlayers().forEach(p -> {
			if (p.getUuid() == e.getPlayer().getUniqueId()) {
				player = p;
				return;
			}
		});

		// Remove player from array
		PlayTimeHelper.getPlayTimePlayers().remove(player);

		// Get time of quit
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String loggedOutAt = formatter.format(new Date());

		// Compare times and calculate seconds between
		try {
			Date d1 = formatter.parse(player.getLastLogin());
			Date d2 = formatter.parse(loggedOutAt);

			long diff = d2.getTime() - d1.getTime(); // in milliseconds
			long diffSeconds = diff / 1000; // in seconds
			long diffMinutes = diffSeconds / 60; // in minutes
			diffSeconds = diffSeconds % 60; // remaining seconds

			long totalSecondsPlayed = diffSeconds + (diffMinutes * 60);

			McPanelX_Core.getInstance().getLogger().info("Player " + e.getPlayer().getName() + " logged out at " + loggedOutAt + " after " + totalSecondsPlayed + " seconds");
			// Add new onlinetime to total playtime
			UserHelper.savePlayTime(e.getPlayer().getUniqueId(), totalSecondsPlayed);
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
