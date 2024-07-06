package xyz.mythicalsystems.mcpanelxcore;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.mythicalsystems.mcpanelxcore.commands.Alert;
import xyz.mythicalsystems.mcpanelxcore.commands.Console;
import xyz.mythicalsystems.mcpanelxcore.events.BungeeKick;
import xyz.mythicalsystems.mcpanelxcore.events.PlayerChatListener;
import xyz.mythicalsystems.mcpanelxcore.events.PlayerTabCompleteListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class McPanelX_Core extends Plugin {
    public static Plugin plugin;
    private static McPanelX_Core instance;
    private SimplifiedConfig cfg;
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    public static String title;
    public static String message;
    public static List<String> blockedCommandMessage;
    public static List<String> blockedCommandMessageAdmin;
    public static List<String> blockedCommands;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        instance = this;
        cfg = new SimplifiedConfig(this, "config.yml", true);

        PluginManager pm = getProxy().getPluginManager();
        if (cfg().getBoolean("InGameConsole.enabled") == true) {
            pm.registerCommand(plugin, new Console());
        }
        if (cfg().getBoolean("CommandBlocker.enabled") == true) {
            pm.registerListener(plugin, new PlayerTabCompleteListener());
            pm.registerListener(plugin, new PlayerChatListener());
        }
        if (cfg().getBoolean("BungeeKick.enabled") == true) {
            pm.registerListener(plugin, new BungeeKick());
        }

        if (cfg().getBoolean("AlertSystem.enabled") == true) {
            pm.registerCommand(plugin, new Alert());
        }

        try {
            McPanelX_Core.makeConfigAlternative();
        } catch (IOException e) {
            getProxy().stop();
            getLogger().severe("Failed to create default config file: " + e);
        }
        final String pluginVersion = getDescription().getVersion();
        if (pluginVersion.contains("SNAPSHOT") || pluginVersion.contains("PRE")) {
            getLogger().warning(
                    "You are running a development build of McPanelX-Core. This version is not stable and may contain bugs.");
        }
        if (pluginVersion.contains("BETA")) {
            getLogger().warning(
                    "You are running a beta build of McPanelX-Core. This version is not stable and may contain bugs.");
        }

        WebServer webServer = new WebServer(cfg().getInt("Panel.port"));
        try {
            webServer.start();
            getLogger().info("Web server started on port " + cfg().getInt("Panel.port"));
        } catch (IOException e) {
            getLogger().severe("Failed to start web server: " + e);
        }
        try {
            McPanelX_Core.title = ChatColor.translateAlternateColorCodes('&', cfg().getString("Messages.AlertTitle"));
            McPanelX_Core.message = ChatColor.translateAlternateColorCodes('&',
                    cfg().getString("Messages.AlertMessage"));
        } catch (Exception e) {
            getLogger().severe("Failed to load alert message: " + e);
        }
        McPanelX_Core.blockedCommandMessage = cfg().getStringList("CommandBlocker.blocked-command-message");
        McPanelX_Core.blockedCommandMessageAdmin = cfg().getStringList("CommandBlocker.blocked-command-message-admin");
        McPanelX_Core.blockedCommands = cfg().getStringList("CommandBlocker.blocked-commands");
        getLogger().info("#========================================#");
        getLogger().info("");
        getLogger().info(colorize("      McPanelX v" + getVersion()));
        getLogger().info(colorize(
                "      Startup Time: " + (System.currentTimeMillis() - startTime) + "&7 ms"));
        getLogger().info(
                colorize("      WebServer enabled on port: " + cfg().getInt("Panel.port")));
        getLogger().info("");
        getLogger().info("#========================================#");
    }

    @Override
    public void onDisable() {
        getLogger().info("#========================================#");
        getLogger().info("");
        getLogger().info(colorize("      McPanelX v" + getVersion()));
        getLogger().info(colorize("      Plugin has been disabled!"));
        getLogger().info("");
        getLogger().info("#========================================#");
    }

    public static McPanelX_Core getInstance() {
        return instance;
    }

    public static String getVersion() {
        return instance.getDescription().getVersion();
    }

    public static Configuration cfg() {
        try {
            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new File(getInstance().getDataFolder(), "config.yml"));
            return configuration;
        } catch (IOException e) {
            getInstance().getLogger().info("[McPanelX] Failed to get config: " + e);
            return null;
        }
    }

    public static String getPrefix() {
        return cfg().getString("Messages.Prefix");
    }

    public static void makeConfigAlternative() throws IOException {
        if (!getInstance().getDataFolder().exists()) {
            getInstance().getDataFolder().mkdir();
        }

        File file = new File(getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getInstance().getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SimplifiedConfig getConfig() {
        return cfg;
    }

    public static String colorize(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String translateHexColorCodes(final String message) {
        final char colorChar = ChatColor.COLOR_CHAR;

        final Matcher matcher = HEX_PATTERN.matcher(message);
        final StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);

        while (matcher.find()) {
            final String group = matcher.group(1);

            matcher.appendReplacement(buffer, colorChar + "x"
                    + colorChar + group.charAt(0) + colorChar + group.charAt(1)
                    + colorChar + group.charAt(2) + colorChar + group.charAt(3)
                    + colorChar + group.charAt(4) + colorChar + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }

    public static boolean equalsIgnoreCase(List<String> list, String searchString) {
        if (list == null || searchString == null)
            return false;
        if (searchString.isEmpty())
            return true;
        for (String string : list) {
            if (string == null)
                continue;
            if (string.equalsIgnoreCase(searchString))
                return true;
        }
        return false;
    }

    public static BaseComponent[] transformString(String string) {
        if (string == null)
            throw new NullPointerException("string cannot be null");
        return TextComponent
                .fromLegacyText(ChatColor.translateAlternateColorCodes('&', McPanelX_Core.getPrefix() + string));
    }

    public static List<String> getBlockedCommandMessage() {
        return Collections.unmodifiableList(McPanelX_Core.blockedCommandMessage);
    }

    public static List<String> getBlockedCommandMessageAdmin() {
        return Collections.unmodifiableList(McPanelX_Core.blockedCommandMessageAdmin);
    }

    public static List<String> getBlockedCommands() {
        return Collections.unmodifiableList(McPanelX_Core.blockedCommands);
    }
}
