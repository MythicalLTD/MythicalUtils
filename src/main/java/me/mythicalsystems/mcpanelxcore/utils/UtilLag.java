package me.mythicalsystems.mcpanelxcore.utils;

import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.PacketEvents;

public class UtilLag {

    public static Integer getPing(Player p) {
        return PacketEvents.getAPI().getPlayerManager().getPing(p);
    }
}