package xyz.mythicalsystems.mcpanelxcore;

import com.github.retrooper.packetevents.PacketEvents;

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
import xyz.mythicalsystems.mcpanelxcore.commands.McPanelXCommand;
import xyz.mythicalsystems.mcpanelxcore.drivers.MySQL;
import xyz.mythicalsystems.mcpanelxcore.events.LoggerEvent;
import xyz.mythicalsystems.mcpanelxcore.events.BungeeJoin;
import xyz.mythicalsystems.mcpanelxcore.events.BungeeKick;
import xyz.mythicalsystems.mcpanelxcore.events.PlayerChatListener;
import xyz.mythicalsystems.mcpanelxcore.events.PlayerTabCompleteListener;
import xyz.mythicalsystems.mcpanelxcore.helpers.BrandHandler;
import xyz.mythicalsystems.mcpanelxcore.helpers.UserHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the main class of the McPanelX_Core plugin.
 */
public final class McPanelX_Core extends Plugin {

    /**
     * The instance of the McPanelX_Core plugin.
     */
    private static McPanelX_Core instance;

    /**
     * The pattern used to match hexadecimal color codes.
     */
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    /**
     * The title used for alerts.
     */
    public static String title;

    /**
     * The message used for alerts.
     */
    public static String message;

    /**
     * The message displayed when a command is blocked for regular players.
     */
    public static List<String> blockedCommandMessage;

    /**
     * The message displayed when a command is blocked for admin players.
     */
    public static List<String> blockedCommandMessageAdmin;

    /**
     * The list of blocked commands.
     */
    public static List<String> blockedCommands;

    /**
     * Plugin module status
     */
    public static boolean inGameConsoleEnabled;
    public static boolean commandBlockerEnabled;
    public static boolean bungeeKickEnabled;
    public static boolean alertSystemEnabled;
    public static boolean loggerEnabled;
    public static boolean playtimeEnabled;

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        instance = this;
        PluginManager pm = getProxy().getPluginManager();
        try {
            McPanelX_Core.makeConfigAlternative();
        } catch (IOException e) {
            getProxy().stop();
            getLogger().severe("Failed to create default config file: " + e);
        }

        try {
            McPanelX_Core.makeMessagesAlternative();
        } catch (IOException e) {
            getProxy().stop();
            getLogger().severe("Failed to create default messages file: " + e);
        }

        if (cfg().getBoolean("InGameConsole.enabled")) {
            getLogger().info("[McPanelX-Core] InGameConsole is enabled!");
            pm.registerCommand(this, new Console());
            inGameConsoleEnabled = true;
        } else {
            getLogger().info("[McPanelX-Core] InGameConsole is disabled!");
            inGameConsoleEnabled = false;
        }

        if (cfg().getBoolean("CommandBlocker.enabled")) {
            McPanelX_Core.blockedCommandMessage = messages().getStringList("CommandBlocker.blocked-message");
            McPanelX_Core.blockedCommandMessageAdmin = messages().getStringList("CommandBlocker.blocked-message-admin");
            McPanelX_Core.blockedCommands = cfg().getStringList("CommandBlocker.blocked-commands");
            pm.registerListener(this, new PlayerTabCompleteListener());
            pm.registerListener(this, new PlayerChatListener()); 
            getLogger().info("[McPanelX-Core] CommandBlocker is enabled!");
            commandBlockerEnabled = true;
        } else {
            getLogger().info("[McPanelX-Core] CommandBlocker is disabled!");
            commandBlockerEnabled = false;
        }

        if (cfg().getBoolean("BungeeKick.enabled")) {
            pm.registerListener(this, new BungeeKick());
            getLogger().info("[McPanelX-Core] BungeeKick is enabled!");
            bungeeKickEnabled = true;
        } else {
            getLogger().info("[McPanelX-Core] BungeeKick is disabled!");
            bungeeKickEnabled = false;
        }

        if (cfg().getBoolean("AlertSystem.enabled")) {
            pm.registerCommand(this, new Alert());
            getLogger().info("[McPanelX-Core] AlertSystem is enabled!");
            alertSystemEnabled = true;
        } else {
            getLogger().info("[McPanelX-Core] AlertSystem is disabled!");
            alertSystemEnabled = false;
        }

        
        if (cfg().getBoolean("Logger.enabled")) {
            if (pm.getPlugin("packetevents") != null) {
                pm.registerListener(this, new LoggerEvent());
                getLogger().info("[McPanelX-Core] PacketEvents plugin is installed!");
                if (cfg().getBoolean("Logger.logClientBranding")) {
                    PacketEvents.getAPI().getEventManager().registerListener(new BrandHandler());
                }
                getLogger().info("[McPanelX-Core] Logger is enabled!");
                loggerEnabled = true;

            } else {
                getLogger().warning("[McPanelX-Core] PacketEvents plugin is not installed!");
                getLogger().warning("[McPanelX-Core] Logger is disabled!");
                loggerEnabled = false;
            }
        } else {
            getLogger().info("[McPanelX-Core] Logger is disabled!");
            loggerEnabled = false;
        }

        if (cfg().getBoolean(("PlayTime.enabled"))) {
            getLogger().info("[McPanelX-Core] Playtime is enabled!");
            pm.registerListener(this, new BungeeJoin());
            playtimeEnabled = true;
        } else {
            getLogger().info("[McPanelX-Core] Playtime is disabled!");
            playtimeEnabled = false;
        }

        if (cfg().getBoolean("Panel.enable_panel_command")) {
            pm.registerCommand(this, new McPanelXCommand());
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
            McPanelX_Core.title = ChatColor.translateAlternateColorCodes('&', messages().getString("Alert.Title"));
            McPanelX_Core.message = ChatColor.translateAlternateColorCodes('&', messages().getString("Alert.Message"));
        } catch (Exception e) {
            getLogger().severe("Failed to load alert message: " + e);
        }

        MySQL mySQL = new MySQL();
        if (mySQL.TryConnection()) {
            getLogger().info("[McPanelX-Core] Connected to MySQL database!");
        } else {
            getLogger().severe("[McPanelX-Core] Failed to connect to MySQL database!");
            getProxy().stop(pluginVersion + " failed to connect to MySQL database!");
        }

        getLogger().info("#========================================#");
        getLogger().info("");
        getLogger().info(colorize("      McPanelX v" + getVersion()));
        getLogger().info(colorize(
                "      Startup Time: " + (System.currentTimeMillis() - startTime) + "&7 ms"));
        getLogger().info(
                colorize("      WebServer enabled on port: " + cfg().getInt("Panel.port")));
        if (inGameConsoleEnabled) {
            getLogger().info(colorize("      InGameConsole enabled!"));
        } else {
            getLogger().info(colorize("      InGameConsole disabled!"));
        }
        if (commandBlockerEnabled) {
            getLogger().info(colorize("      CommandBlocker enabled!"));
        } else {
            getLogger().info(colorize("      CommandBlocker disabled!"));
        }
        if (bungeeKickEnabled) {
            getLogger().info(colorize("      BungeeKick enabled!"));
        } else {
            getLogger().info(colorize("      BungeeKick disabled!"));
        }
        if (alertSystemEnabled) {
            getLogger().info(colorize("      AlertSystem enabled!"));
        } else {
            getLogger().info(colorize("      AlertSystem disabled!"));
        }
        if (loggerEnabled) {
            getLogger().info(colorize("      Logger enabled!"));
        } else {
            getLogger().info(colorize("      Logger disabled!"));
        }
        if (playtimeEnabled) {
            getLogger().info(colorize("      Playtime enabled!"));
        } else {
            getLogger().info(colorize("      Playtime disabled!"));
        }
        getLogger().info("");
        getLogger().info("#========================================#");
        if (cfg().getBoolean("Logger.enabled") == true && loggerEnabled == false) {
            for (int i = 0; i < 25; i++) {
                getLogger().severe("Logger is enabled but PacketEvents is not installed! (Logger will not work)");
            }
        } 
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        try {
            UserHelper.markHoleServerAsOffline();
        } catch (SQLException e) {
            getLogger().severe("[McPanelX-Core] Failed to mark server as offline: " + e);
        }
        getLogger().info("#========================================#");
        getLogger().info("");
        getLogger().info(colorize("      McPanelX v" + getVersion()));
        getLogger().info(colorize("      Plugin has been disabled!"));
        getLogger().info("");
        getLogger().info("#========================================#");
    }
    /**
     * Reload the plugin
     */
    public static boolean Reload() {
        return false; // This cant be done in BungeeCord due to MySQL and desync reason!
    }

    /**
     * Gets the instance of the McPanelX_Core plugin.
     *
     * @return The instance of the plugin.
     */
    public static McPanelX_Core getInstance() {
        return instance;
    }

    /**
     * Gets the version of the McPanelX_Core plugin.
     *
     * @return The version of the plugin.
     */
    public static String getVersion() {
        return instance.getDescription().getVersion();
    }

    /**
     * Gets the configuration of the plugin.
     *
     * @return The configuration.
     */
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

    /**
     * Gets the messages configuration of the plugin.
     *
     * @return The messages configuration.
     */
    public static Configuration messages() {
        try {
            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new File(getInstance().getDataFolder(), "messages.yml"));
            return configuration;
        } catch (IOException e) {
            getInstance().getLogger().info("[McPanelX] Failed to get messages: " + e);
            return null;
        }
    }

    /**
     * Gets the prefix used for messages.
     *
     * @return The message prefix.
     */
    public static String getPrefix() {
        return colorize(messages().getString("Global.Prefix"));
    }

    /**
     * Creates an alternative configuration file if it doesn't exist.
     *
     * @throws IOException If an I/O error occurs.
     */
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
            } finally {
                getInstance().getLogger().info("Created config.yml");
            }
        }
    }

    /**
     * Creates an alternative messages configuration file if it doesn't exist.
     *
     * @throws IOException If an I/O error occurs.
     */
    public static void makeMessagesAlternative() throws IOException {
        if (!getInstance().getDataFolder().exists()) {
            getInstance().getDataFolder().mkdir();
        }

        File file = new File(getInstance().getDataFolder(), "messages.yml");

        if (!file.exists()) {
            try (InputStream in = getInstance().getResourceAsStream("messages.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                getInstance().getLogger().info("Created messages.yml");
            }
        }
    }

    /**
     * Colorizes a message by replacing color codes with the corresponding color.
     *
     * @param message The message to colorize.
     * @return The colorized message.
     */
    public static String colorize(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Translates hexadecimal color codes in a message to the corresponding color.
     *
     * @param message The message to translate.
     * @return The translated message.
     */
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

    /**
     * Checks if a list of strings contains a case-insensitive match for a search
     * string.
     *
     * @param list         The list of strings to search.
     * @param searchString The search string.
     * @return true if a match is found, false otherwise.
     */
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

    /**
     * Transforms a string into a BaseComponent array with color codes applied.
     *
     * @param string The string to transform.
     * @return The transformed BaseComponent array.
     */
    public static BaseComponent[] transformString(String string) {
        if (string == null)
            throw new NullPointerException("string cannot be null");
        return TextComponent
                .fromLegacyText(ChatColor.translateAlternateColorCodes('&', McPanelX_Core.getPrefix() + string));
    }

    /**
     * Gets the list of blocked command messages.
     *
     * @return The list of blocked command messages.
     */
    public static List<String> getBlockedCommandMessage() {
        return Collections.unmodifiableList(McPanelX_Core.blockedCommandMessage);
    }

    /**
     * Gets the list of blocked admin command messages.
     *
     * @return The list of blocked admin command messages.
     */
    public static List<String> getBlockedCommandMessageAdmin() {
        return Collections.unmodifiableList(McPanelX_Core.blockedCommandMessageAdmin);
    }

    /**
     * Gets the list of blocked commands.
     *
     * @return The list of blocked commands.
     */
    public static List<String> getBlockedCommands() {
        return Collections.unmodifiableList(McPanelX_Core.blockedCommands);
    }
}
