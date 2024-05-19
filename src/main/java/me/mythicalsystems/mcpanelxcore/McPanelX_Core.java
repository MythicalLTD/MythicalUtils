package me.mythicalsystems.mcpanelxcore;

import me.mythicalsystems.mcpanelxcore.commands.ChatFormater;
import me.mythicalsystems.mcpanelxcore.commands.Console;
import me.mythicalsystems.mcpanelxcore.commands.HaxDex;
import me.mythicalsystems.mcpanelxcore.commands.McPanelX;
import me.mythicalsystems.mcpanelxcore.events.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import org.bukkit.plugin.Plugin;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.mythicalsystems.mcpanelxcore.database.connection;

public final class McPanelX_Core extends JavaPlugin {
    public static FileConfiguration config;
    public static File log;
    public static Plugin plugin;
    private connection database;
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    public boolean is19Server = true;
    public boolean is13Server = false;
    public boolean oldEngine = false;
    public static boolean isChatLocked;
    private static McPanelX_Core instance;

    /**
     * Get the plugin version number!
     *
     * @return The version of the plugin
     */
    public static String getVersion() {
        return "1.0.0.0";
    }

    /**
     * The scripts that run on the startup of the plugin
     */
    @Override
    public void onEnable() {
        instance = this;
        getMcVersion();
        final String pluginVersion = getDescription().getVersion();
        if (pluginVersion.contains("SNAPSHOT") || pluginVersion.contains("PRE")) {
            getLogger().warning(
                    "You are running a development build of McPanelX-Core. This version is not stable and may contain bugs.");
                    
        } if (pluginVersion.contains("BETA")) {
            getLogger().warning(
                    "You are running a beta build of McPanelX-Core. This version is not stable and may contain bugs.");
        }
        McPanelX_Core.isChatLocked = false;
        long startTime = System.currentTimeMillis();
        try {
            config = getConfig();
            config.options().copyDefaults(true);
            File configs = new File(getDataFolder() + File.separator + "config.yml");
            if (!configs.exists()) {
                getLogger().info("Creating default config file!");
                config.save(configs);
            } else {
                getLogger().info("Using config file!");
            }
        } catch (Exception ex) {
            Bukkit.getLogger().info("[McPanelX-Core] Failed to create the config file: " + ex.toString());
        }
        (new File(getDataFolder().getAbsolutePath())).mkdirs();
        log = new File(getDataFolder().getAbsolutePath() + File.separator + "log.txt");
        if (!log.exists()) {
            try {
                log.createNewFile();
            } catch (IOException ex) {
                Bukkit.getLogger()
                        .info(McPanelX_Core.colorize("§7[§c§lMcPanelX-Core§7] Failed to create the log file: ")
                                + ex.toString());
            }
        }
        WebServer webServer = new WebServer(config.getInt("Panel.port"));
        try {
            webServer.start();
        } catch (Exception e) {
            Bukkit.getLogger().info("Failed to start web server: " + e.toString());
        }
        this.database = new connection();
        try {
            this.database.initializeDatabase();
        } catch (SQLException e) {
            Bukkit.getLogger().info("[MCPanelX-Core] Failed to init database: " + e.toString());
            Bukkit.getServer().shutdown();
        }
        config = getConfig();
        plugin = (Plugin) this;
        try {
            this.database.markHoleServerAsOffline();
        } catch (SQLException e) {
            Bukkit.getLogger().info("[MCPanelX-Core] Failed to mark all players as offline: " + e.toString());
        }

        // Index the console command!
        getCommand("haxdex").setExecutor(new HaxDex());

        if (config.getBoolean("InGameConsole.enabled") == true) {
            getCommand("console").setExecutor(new Console());
        }
        if (config.getBoolean("AntiSyntax.enabled") == true) {
            getServer().getPluginManager().registerEvents(new SyntaxBlocker(), (Plugin) this);
        }
        if (config.getBoolean("ChatFormater.enabled") == true) {
            getServer().getPluginManager().registerEvents(new ChatFormat(), (Plugin) this);
            getCommand("chatformater").setExecutor(new ChatFormater());
        }
        if (config.getBoolean("Database.enabled") == true) {
            getCommand("mcpanelx").setExecutor(new McPanelX());
            getServer().getPluginManager().registerEvents(new ChatSaveEvent(database), (Plugin) this);
            getServer().getPluginManager().registerEvents(new CommandSaveEvent(database), (Plugin) this);
            getServer().getPluginManager().registerEvents(new ConsoleSaveCommand(database), (Plugin) this);
            getServer().getPluginManager().registerEvents(new PlayTime(database), (Plugin) this);
            if (config.getBoolean("CustomMessages.enabled") == true) {
                getServer().getPluginManager().registerEvents(new JoinEventHandler(database), (Plugin) this);
            }
        }

        if (config.getBoolean("CommandBlocker.enabled") == true) {
            getServer().getPluginManager().registerEvents(new ConsoleCommandBlock(), (Plugin) this);
        }
        getServer().getPluginManager().registerEvents(new AntiCheat(), (Plugin) this);
        getServer().getPluginManager().registerEvents(new AntiDisconnectSpam(), (Plugin) this);
        getServer().getPluginManager().registerEvents(new AntiUserSteal(), (Plugin) this);

        getLogger().info("#========================================#");
        getLogger().info("");
        getLogger().info(colorize("      McPanelX v" + getVersion()));
        if (config.getBoolean("Database.enabled") == true) {
            getLogger().info(colorize("      Database enabled&r ("
                    + config.getString("Database.host") + ":" + config.getString("Database.port") + ")"));
        } else {
            getLogger().info(colorize("      Database disabled"));
        }
        if (config.getBoolean("InGameConsole.enabled") == true) {
            getLogger().info(colorize("      InGameConsole enabled"));
        } else {
            getLogger().info(colorize("      InGameConsole disabled"));
        }
        if (config.getBoolean("CustomMessage.enabled") == true) {
            getLogger().info(colorize("      CustomMessage enabled"));
        } else {
            getLogger().info(colorize("      CustomMessage disabled"));
        }
        if (config.getBoolean("AntiSyntax.enabled") == true) {
            getLogger().info(colorize("      AntiSyntax enabled"));
        } else {
            getLogger().info(colorize("      AntiSyntax disabled"));

        }
        if (config.getBoolean("ChatFormater.enabled") == true) {
            getLogger().info(colorize("      ChatFormater enabled"));
        } else {
            getLogger().info(colorize("      ChatFormater disabled"));
        }
        getLogger().info(colorize("      AntiDisconnectSpam enabled"));
        getLogger().info(colorize("      AntiUserSteal enabled"));
        getLogger().info(colorize(
                "      Startup Time: " + (System.currentTimeMillis() - startTime) + "&7 ms"));
        getLogger().info(
                colorize("      WebServer enabled on port: " + config.getInt("Panel.port")));
        getLogger().info("");
        getLogger().info("#========================================#");
    }

    /**
     * Get the plugin prefix from the config file!
     *
     * @return String the prefix
     */
    public static String getPrefix() {
        return config.getString("Messages.Prefix").replace("&", "§");
    }

    /**
     * Send a message to either the server console or the player
     * 
     * @param sender  The sender
     * @param message The message you want to the target
     *
     */
    public static void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage(message);
        } else {
            Bukkit.getLogger().info(colorize("[McPanelX] " + message));
        }
    }

    /**
     * The thing that I run to stop the plugin!
     */
    @Override
    public void onDisable() {
        try {
            this.database.markHoleServerAsOffline();
        } catch (SQLException e) {
            Bukkit.getLogger().info("[MCPanelX-Core] Failed to mark all players as offline: " + e.toString());
        }
        getLogger().info("#========================================#");
        getLogger().info("");
        getLogger().info("      McPanelX v" + getVersion());
        getLogger().info("        Plugin disabled");
        getLogger().info("");
        getLogger().info("#========================================#");
    }

    /**
     * Add colors ;)
     * 
     * @param message The message you want to colorize
     *
     * @return The new colored message!
     */
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

    /**
     * Get a player using its uuid
     *
     * @param uuid The uuid of the player
     * @return
     */
    public static Player getPlayerByUUID(UUID uuid) {
        return (Player) Bukkit.getOfflinePlayer(uuid);
    }

    private void getMcVersion() {
        String[] serverVersion = Bukkit.getBukkitVersion().split("-");
        String version = serverVersion[0];
        Bukkit.getLogger().info("Server version detected: " + version);
        if (version.matches("1.7.10") || version.matches("1.7.9") || version.matches("1.7.5")
                || version.matches("1.7.2") || version.matches("1.8.8") || version.matches("1.8.3")
                || version.matches("1.8.4") || version.matches("1.8")) {
            this.is19Server = false;
            this.is13Server = false;
            this.oldEngine = true;
        } else if (version.matches("1.9") || version.matches("1.9.1") || version.matches("1.9.2")
                || version.matches("1.9.3") || version.matches("1.9.4") || version.matches("1.10")
                || version.matches("1.10.1") || version.matches("1.10.2") || version.matches("1.11")
                || version.matches("1.11.1") || version.matches("1.11.2")) {
            this.oldEngine = true;
            this.is19Server = true;
            this.is13Server = false;
        } else if (version.matches("1.13") || version.matches("1.13.1") || version.matches("1.13.2")) {
            this.oldEngine = false;
            this.is19Server = true;
            this.is13Server = true;
        } else if (version.matches("1.14") || version.matches("1.14.1") || version.matches("1.14.2")
                || version.matches("1.14.3") || version.matches("1.14.4") || version.matches("1.15")) {
            this.oldEngine = false;
            this.is19Server = true;
            this.is13Server = true;
        } else if (version.matches("1.15") || version.matches("1.15.1") || version.matches("1.15.2")) {
            this.oldEngine = false;
            this.is19Server = true;
            this.is13Server = true;
        } else if (version.matches("1.16") || version.matches("1.16.1") || version.matches("1.16.2")
                || version.matches("1.16.3") || version.matches("1.16.4") || version.matches("1.16.5")) {
            this.oldEngine = false;
            this.is19Server = true;
            this.is13Server = true;
        } else if (version.matches("1.17") || version.matches("1.17.1")) {
            this.oldEngine = false;
            this.is19Server = true;
            this.is13Server = true;
        } else if (version.matches("1.18") || version.matches("1.18.1") || version.matches("1.18.2")) {
            this.oldEngine = false;
            this.is19Server = true;
            this.is13Server = true;
        } else if (version.matches("1.19") || version.matches("1.19.1") || version.matches("1.19.2")
                || version.matches("1.19.3") || version.matches("1.19.4")) {
            this.oldEngine = false;
            this.is19Server = true;
            this.is13Server = true;
        } else {
            this.is13Server = true;
            this.is19Server = true;
            this.oldEngine = false;
        }
    }

    public static McPanelX_Core getInstance() {
        return instance;
    }

}
