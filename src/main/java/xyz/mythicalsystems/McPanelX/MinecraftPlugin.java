package xyz.mythicalsystems.McPanelX;


import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public final class MinecraftPlugin extends Plugin {

    /**
     * The instance of the plugin
     */
    public static MinecraftPlugin instance;
    public static PluginManager pluginManager;
    /** 
     * Plugin info 
    */
    public static String version;
    public static String author;
    public static String name;

    @Override
    public void onEnable() {
        instance = this;

        if (pluginManager.getPlugin("packetevents") == null) {
            getLogger().severe("PacketEvents is not installed! Please install it to use McPanelX-Core");
            getProxy().stop();
        } else {
            getLogger().info("PacketEvents is installed! McPanelX-Core is ready to go!");
        }
        // Plugin startup logic
        version = getDescription().getVersion();
        author = getDescription().getAuthor();
        name = getDescription().getName();
        pluginManager = getProxy().getPluginManager();
        McPanelX.up();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        McPanelX.down();
    }

    public static MinecraftPlugin getInstance() {
        return instance;
    }
}
