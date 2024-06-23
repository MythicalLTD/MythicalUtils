package me.mythicalsystems.mcpanelxcore.handlers;

import java.util.List;

import org.bukkit.entity.Player;

public interface Connection {
    boolean isValid();

    me.mythicalsystems.mcpanelxcore.handlers.ProtocolVersion getProtocol(Player player);

    me.mythicalsystems.mcpanelxcore.handlers.ProtocolVersion getServerProtocol();

    List<me.mythicalsystems.mcpanelxcore.handlers.ProtocolVersion> getSupportedProtocols();
}
