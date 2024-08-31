package xyz.mythicalsystems.McPanelX;

import java.sql.Connection;

import com.github.retrooper.packetevents.PacketEvents;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import xyz.mythicalsystems.McPanelX.commands.McPanelXCommand;
import xyz.mythicalsystems.McPanelX.events.ChatEvent;
import xyz.mythicalsystems.McPanelX.events.JoinEvent;
import xyz.mythicalsystems.McPanelX.events.LeaveEvent;
import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Link.Account;
import xyz.mythicalsystems.McPanelX.src.Logger.Logger;
import xyz.mythicalsystems.McPanelX.src.Messages.Messages;
import xyz.mythicalsystems.McPanelX.src.Metrics.Metrics;
import xyz.mythicalsystems.McPanelX.src.MySQL.MySQLConnector;
import xyz.mythicalsystems.McPanelX.src.Translators.ChatTranslator;
import xyz.mythicalsystems.McPanelX.src.Translators.VersionTranslator;

public class McPanelX {
    /**
     * The path to the plugin
     */
    public static String plugin_path;
    /**
     * The start time of the plugin
     */
    public static long startTime;

    /**
     * The logger for the plugin
     */
    public static Logger logger;
    /**
     * The MySQL connector for the plugin
     */
    public static MySQLConnector mysql;
    /**
     * The mysql connection
     */
    public static Connection connection;
    /**
     * Start the plugin
     */
    public static void up() {
        startTime = System.currentTimeMillis();
        plugin_path = MinecraftPlugin.getInstance().getDataFolder().getAbsolutePath();
        // Initialize the logger
        logger = new Logger();
        logger.info("McPanelX", "Starting McPanelX");
        // Initialize the config
        Config.init();
        // Initialize the messages
        Messages.init();
        // Initialize metrics
        new Metrics(MinecraftPlugin.getInstance(),23007);
        // Show plugin info
        VersionTranslator.TranslatePluginStartup();
        // Initialize the database connection!
        mysql = new MySQLConnector(Config.getSetting().getString("Database.Host"),Config.getSetting().getString("Database.Port"),Config.getSetting().getString("Database.Database"),Config.getSetting().getString("Database.Username"),Config.getSetting().getString("Database.Password"));
        mysql.tryConnection();
        try {
            connection = mysql.getHikari().getConnection();
        } catch (Exception e) {
            mysql.reconnect();
            logger.error("McPanelX", "Failed to connect to the MySQL server: " + e.getMessage());
            return;
        }
        // Register events

        MinecraftPlugin.pluginManager.registerListener(MinecraftPlugin.getInstance(), new LeaveEvent());
        MinecraftPlugin.pluginManager.registerListener(MinecraftPlugin.getInstance(), new ChatEvent());
        PacketEvents.getAPI().getEventManager().registerListener(new JoinEvent());

        //Register commands
        MinecraftPlugin.pluginManager.registerCommand(MinecraftPlugin.getInstance(), new McPanelXCommand());

        // Show that the plugin is ready to go
        logger.info("McPanelX", "McPanelX is ready to go!");
        logger.info("McPanelX", "Startup took " + (System.currentTimeMillis() - startTime) + "ms");
    }   

    /**
     * Stop the plugin
     */
    public static void down() {
        logger.info("McPanelX", "McPanelX is stopping NOW!");
        Account.markHoleServerAsOffline();
        mysql.close();
        logger.info("McPanelX", "Goodbye!");
    }

    /**
     * Reload the plugin
     */
    public static void reload() {
        for (ProxiedPlayer player : MinecraftPlugin.getInstance().getProxy().getPlayers()) {
            player.disconnect(new TextComponent(ChatTranslator.Translate(Messages.getMessage().getString("Global.ReloadKickReason"))));
        }
        down();
        up();
    }
}
