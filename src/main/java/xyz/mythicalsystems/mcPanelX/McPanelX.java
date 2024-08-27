package xyz.mythicalsystems.McPanelX;

import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Logger.Logger;
import xyz.mythicalsystems.McPanelX.src.Messages.Messages;
import xyz.mythicalsystems.McPanelX.src.Metrics.Metrics;
import xyz.mythicalsystems.McPanelX.src.MySQL.MySQLConnector;
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
        
    }   

    /**
     * Stop the plugin
     */
    public static void down() {
        mysql.close();
    }

    /**
     * Reload the plugin
     */
    public static void reload() {
        down();
        up();
    }
}
