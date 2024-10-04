package xyz.mythicalsystems.mythicalsystemsutils.handlers;

import java.util.List;

import org.bukkit.entity.Player;

public interface Connection {
    boolean isValid();

    xyz.mythicalsystems.mythicalsystemsutils.handlers.ProtocolVersion getProtocol(Player player);

    xyz.mythicalsystems.mythicalsystemsutils.handlers.ProtocolVersion getServerProtocol();

    List<xyz.mythicalsystems.mythicalsystemsutils.handlers.ProtocolVersion> getSupportedProtocols();
}
