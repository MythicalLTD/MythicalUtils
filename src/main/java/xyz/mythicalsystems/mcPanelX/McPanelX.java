package xyz.mythicalsystems.McPanelX;

import xyz.mythicalsystems.McPanelX.src.Config.Config;
import xyz.mythicalsystems.McPanelX.src.Logger.Logger;
import xyz.mythicalsystems.McPanelX.src.Messages.Messages;
import xyz.mythicalsystems.McPanelX.src.Metrics.Metrics;
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

        logger.info("McPanelX",ChatTranslator.Translate("{prefix} {textcolor}welcome to {maincolor}here lol so {secondarycolor}hahaha"));
    }

    /**
     * Stop the plugin
     */
    public static void down() {

    }

    /**
     * Reload the plugin
     */
    public static void reload() {
        down();
        up();
    }
}
