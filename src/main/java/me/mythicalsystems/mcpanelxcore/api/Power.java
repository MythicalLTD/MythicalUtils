package me.mythicalsystems.mcpanelxcore.api;

import fi.iki.elonen.NanoHTTPD.Response;
import me.mythicalsystems.mcpanelxcore.WebServer;

import java.util.Map;

import org.bukkit.Bukkit;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;

public class Power {
    public static Response Request(IHTTPSession session) {
        Method method = session.getMethod();
        if (method == Method.PUT) {
            return HandlePUTRequest(session);
        } else {
            return WebServer.BadRequest("Invalid request method!", null);
        }
    }

    public static Response HandlePUTRequest(IHTTPSession session) {
        try {
            Map<String, String> params = session.getParms();
                String action = params.get("action");

                if (action == null) {
                    return WebServer.BadRequest("Please provide a action!", null);
                }

                if (action.equals("restart")) {
                    return RestartServer();
                } else if (action.equals("stop")) {
                    return StopServer();
                }
                else {
                    return WebServer.BadRequest("Invalid action!", null);
                }

            } catch (Exception e) {
            return WebServer.InternalServerError("Failed to execute command: " + e.getMessage(), null);
        }
    }

    public static Response RestartServer() {
        try {
            Bukkit.spigot().restart();
            return WebServer.OK("Server is restarting!", null);
        } catch (Exception e) {
            return WebServer.InternalServerError("Failed to restart server: " + e.getMessage(), null);
        }
    }

    public static Response StopServer() {
        try {
            Bukkit.getServer().shutdown();
            return WebServer.OK("Server is stopping!", null);
        } catch (Exception e) {
            return WebServer.InternalServerError("Failed to stop server: " + e.getMessage(), null);
        }
    }
}
