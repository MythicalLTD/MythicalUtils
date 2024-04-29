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
                Bukkit.getLogger().info("§7[§c§lMcPanelX-Core§7] Failed to create the log file: " + ex.toString());
            }
        }
        this.database = new connection();
        try {
            this.database.initializeDatabase();
        } catch (SQLException e) {
            Bukkit.getLogger().info("[MCPanelX-Core] Failed to init database: "+e.toString());
            Bukkit.getServer().shutdown();
        }
        config = getConfig();
        plugin = (Plugin)this;

        //Index the console command!
        getCommand("haxdex").setExecutor(new HaxDex());

        if (config.getBoolean("InGameConsole.enabled") == true) {
            getCommand("console").setExecutor(new Console());
        }
        if (config.getBoolean("CustomJoinMessage.enabled") == true) {
            getServer().getPluginManager().registerEvents(new JoinEventHandler(), (Plugin)this);
        }
        if (config.getBoolean("AntiSyntax.enabled") == true) {
            getServer().getPluginManager().registerEvents(new SyntaxBlocker(), (Plugin)this);
        }
        if (config.getBoolean("ChatFormater.enabled") == true) {
            getServer().getPluginManager().registerEvents(new ChatFormat(), (Plugin) this);
            getCommand("chatformater").setExecutor(new ChatFormater());
        }
        if (config.getBoolean("Database.enabled") == true) {
            getCommand("mcpanelx").setExecutor(new McPanelX());
            getServer().getPluginManager().registerEvents(new ChatSaveEvent(database), (Plugin)this);
            getServer().getPluginManager().registerEvents(new CommandSaveEvent(database), (Plugin)this);
            getServer().getPluginManager().registerEvents(new ConsoleSaveCommand(database), (Plugin)this);
        }

        if (config.getBoolean("CommandBlocker.enabled") == true) {
            getServer().getPluginManager().registerEvents(new ConsoleCommandBlock(), (Plugin)this);
        }

        getServer().getPluginManager().registerEvents(new AntiDisconnectSpam(), (Plugin)this);
        getServer().getPluginManager().registerEvents(new AntiUserSteal(), (Plugin)this);
        getServer().getPluginManager().registerEvents(new PlayTime(database), (Plugin)this);

        getLogger().info("#========================================#");
        getLogger().info("");
        getLogger().info("      McPanelX v"+getVersion());
        getLogger().info("      Startup Time: " + (System.currentTimeMillis() - startTime) + " ms");
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
     * @param sender The sender
     * @param message The message you want to the target
     *
     */
    public static void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage(message);
        } else {
            Bukkit.getLogger().info(colorize("[McPanelX] "+message));
        }
    }

    /**
     * The thing that I run to stop the plugin!
     */
    @Override
    public void onDisable() {
        getLogger().info("#========================================#");
        getLogger().info("");
        getLogger().info("      McPanelX v"+getVersion());
        getLogger().info("        Plugin disabled");
        getLogger().info("");
        getLogger().info("#========================================#");
    }

    /**
     * Add colors ;)
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
        return (Player)Bukkit.getOfflinePlayer(uuid);
    }

}
