package xyz.mythicalsystems.mythicalsystemsutils.events;

import org.bukkit.event.Listener;
import java.util.stream.Collectors;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import xyz.mythicalsystems.mythicalsystemsutils.MythicalSystemsUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.clip.placeholderapi.PlaceholderAPI;

import static org.bukkit.Bukkit.getServer;

public class ChatFormat implements Listener {
        private LuckPerms luckPerms;

        public ChatFormat() {
                this.luckPerms = getServer().getServicesManager().load(LuckPerms.class);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onChat(final AsyncPlayerChatEvent event) {
                final String message = event.getMessage();
                final Player player = event.getPlayer();

                // Get a LuckPerms cached metadata for the player.
                final CachedMetaData metaData = this.luckPerms.getPlayerAdapter(Player.class).getMetaData(player);
                final String group = metaData.getPrimaryGroup();

                String format = MythicalSystemsUtils.config
                                .getString(MythicalSystemsUtils.config
                                                .getString("ChatFormatter.group-formats." + group) != null
                                                                ? "ChatFormatter.group-formats." + group
                                                                : "ChatFormatter.chat-format")
                                .replace("{prefix}", metaData.getPrefix() != null ? metaData.getPrefix() : "")
                                .replace("{suffix}", metaData.getSuffix() != null ? metaData.getSuffix() : "")
                                .replace("{prefixes}",
                                                metaData.getPrefixes().keySet().stream()
                                                                .map(key -> metaData.getPrefixes().get(key))
                                                                .collect(Collectors.joining()))
                                .replace("{suffixes}",
                                                metaData.getSuffixes().keySet().stream()
                                                                .map(key -> metaData.getSuffixes().get(key))
                                                                .collect(Collectors.joining()))
                                .replace("{world}", player.getWorld().getName())
                                .replace("{name}", player.getName())
                                .replace("{displayname}", player.getDisplayName())
                                .replace("{username-color}",
                                                metaData.getMetaValue("username-color") != null
                                                                ? metaData.getMetaValue("username-color")
                                                                : "")
                                .replace("{message-color}",
                                                metaData.getMetaValue("message-color") != null
                                                                ? metaData.getMetaValue("message-color")
                                                                : "");

                format = MythicalSystemsUtils.colorize(
                                MythicalSystemsUtils.translateHexColorCodes(
                                                getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")
                                                                ? PlaceholderAPI.setPlaceholders(player, format)
                                                                : format));

                event.setFormat(format.replace("{message}",
                                player.hasPermission("mcpanelx.colorcodes") && player.hasPermission("mcpanelx.rgbcodes")
                                                ? MythicalSystemsUtils.colorize(MythicalSystemsUtils.translateHexColorCodes(message))
                                                : player.hasPermission("mcpanelx.colorcodes")
                                                                ? MythicalSystemsUtils.colorize(message)
                                                                : player.hasPermission("mcpanelx.rgbcodes")
                                                                                ? MythicalSystemsUtils.translateHexColorCodes(
                                                                                                message)
                                                                                : message)
                                .replace("%", "%%"));
        }
}
