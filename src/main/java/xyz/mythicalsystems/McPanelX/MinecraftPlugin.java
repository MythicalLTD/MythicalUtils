package xyz.mythicalsystems.McPanelX;

import net.md_5.bungee.api.plugin.Plugin;

public final class MinecraftPlugin extends Plugin {

    public static MinecraftPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MinecraftPlugin getInstance() {
        return instance;
    }
}
