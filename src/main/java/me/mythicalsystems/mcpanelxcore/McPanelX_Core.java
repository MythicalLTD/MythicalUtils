package me.mythicalsystems.mcpanelxcore;

import me.mythicalsystems.mcpanelxcore.commands.Version;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;

public final class McPanelX_Core extends JavaPlugin implements Listener {
    public static FileConfiguration config;
    public static File log;
    public static Plugin plugin;
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
        config = getConfig();
        plugin = (Plugin)this;
        //Index the console command!
        getCommand("console").setExecutor(new ConsoleCMD());
        getCommand("version").setExecutor(new Version());

        getServer().getPluginManager().registerEvents(this, (Plugin)this);
        getLogger().info("#========================================#");
        getLogger().info("");
        getLogger().info("      McPanelX v"+getVersion());
        getLogger().info("      Startup Time: " + (System.currentTimeMillis() - startTime) + " ms");
        getLogger().info("");
        getLogger().info("#========================================#");


    }
    public static String getPrefix() {
        return config.getString("Messages.Prefix").replace("&", "§");
    }

    public static String getVersion() {
        return "1.0.0.0";
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String playerName = e.getPlayer().getName();
        String joinMessage = config.getString("Messages.JoinMessage");
        joinMessage = joinMessage.replace("%player%", playerName);
        e.setJoinMessage(joinMessage);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        String playerName = e.getPlayer().getName();
        String quitMessage = config.getString("Messages.LeaveMessage");
        quitMessage = quitMessage.replace("%player%", playerName);
        e.setQuitMessage(quitMessage);
    }

    @EventHandler
    public void AntiUserSteal(PlayerLoginEvent e) {
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            String V1 = e.getPlayer().getName().toLowerCase();
            String Vu = players.getName().toLowerCase();
            if (V1.equals(Vu)) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED+config.getString("Messages.AntiUserSteal"));
            } else {
                e.allow();
            }
        }
    }

    @Override
    public void onDisable() {
        //TODO: Disconnect from MySQL
        getLogger().info("#========================================#");
        getLogger().info("");
        getLogger().info("      McPanelX v"+getVersion());
        getLogger().info("        Plugin disabled");
        getLogger().info("");
        getLogger().info("#========================================#");
    }

}
