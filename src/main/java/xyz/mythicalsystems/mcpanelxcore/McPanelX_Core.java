package xyz.mythicalsystems.mcpanelxcore;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.mythicalsystems.mcpanelxcore.commands.Console;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class McPanelX_Core extends Plugin {
    public static Plugin plugin;
    private static McPanelX_Core instance;
    private SimplifiedConfig cfg;
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = getProxy().getPluginManager();
        if (cfg().getBoolean("InGameConsole.Enabled")) {
            pm.registerCommand(plugin, new Console());
        }
        cfg = new SimplifiedConfig(this, "config.yml", true);
        try {
            McPanelX_Core.makeConfigAlternative();
        } catch (IOException e) {
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
        // long startTime = System.currentTimeMillis();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static McPanelX_Core getInstance() {
        return instance;
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

}
