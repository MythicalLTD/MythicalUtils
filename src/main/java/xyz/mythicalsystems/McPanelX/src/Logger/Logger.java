package xyz.mythicalsystems.McPanelX.src.Logger;

import xyz.mythicalsystems.McPanelX.McPanelX;
import xyz.mythicalsystems.McPanelX.MinecraftPlugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.io.Files;

import net.md_5.bungee.api.ChatColor;

public class Logger {

    /**
     * Constructor for the Logger class
     */
    public Logger() {
        String pluginPath = McPanelX.plugin_path;
        File directory = new File(pluginPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File logFile = new File(pluginPath + "/log.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (Exception e) {
                MinecraftPlugin.getInstance().getLogger().severe("Failed to create log file");
            }
        }
    }

    /**
     * Log an error message
     * 
     * @param class_name The name of the class
     * @param message    The message to log
     */
    public void error(String class_name, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String formattedDate = dateFormat.format(new Date());
        String logMessage = "[" + formattedDate + "] ERROR (" + class_name + "): " + message;

        MinecraftPlugin.getInstance().getLogger().severe(message);
        writeLog(logMessage);
    }

    /**
     * Log an info message
     * 
     * @param class_name The name of the class
     * @param message    The message to log
     */
    public void info(String class_name, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String formattedDate = dateFormat.format(new Date());
        String logMessage = "[" + formattedDate + "] INFO (" + class_name + "): " + message;

        MinecraftPlugin.getInstance().getLogger().info(message);
        writeLog(logMessage);
    }

    /**
     * Log a warning message
     * 
     * @param class_name The name of the class
     * @param message    The message to log
     */
    public void warn(String class_name, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String formattedDate = dateFormat.format(new Date());
        String logMessage = "[" + formattedDate + "] WARN (" + class_name + "): " + message;

        MinecraftPlugin.getInstance().getLogger().warning(message);
        writeLog(logMessage);
    }

    /**
     * Log a debug message
     * 
     * @param class_name The name of the class
     * @param message    The message to log
     */
    public void debug(String class_name, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String formattedDate = dateFormat.format(new Date());
        String logMessage = "[" + formattedDate + "] INFO (" + class_name + "): " + message;

        MinecraftPlugin.getInstance().getLogger().info(message);
        writeLog(logMessage);
    }

    /**
     * Write a message to the log file
     * 
     * @param message
     */
    private void writeLog(String message) {
        File logFile = new File(McPanelX.plugin_path + "/log.txt");
        message = ChatColor.stripColor(message);
        
        try {
            Files.asCharSink(logFile, java.nio.charset.StandardCharsets.UTF_8).write(message);
        } catch (Exception e) {
            MinecraftPlugin.getInstance().getLogger().severe("Failed to write to log file");
        }
    }
}
