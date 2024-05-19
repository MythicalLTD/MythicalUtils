package me.mythicalsystems.mcpanelxcore.api;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import me.mythicalsystems.mcpanelxcore.McPanelX_Core;
import me.mythicalsystems.mcpanelxcore.WebServer;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;

public class Console {

    public static Response Request(IHTTPSession session) {
        Method method = session.getMethod();
        if (method == Method.PUT) {
            return HandlePUTRequest(session);
        } else if (method == Method.GET) {
            return HandleGETRequest(session);
        } else {
            return WebServer.BadRequest("Invalid request method!", null);
        }
    }

    public static Response HandleGETRequest(IHTTPSession session) {
        return GetLatestLogs();
    }

    public static Response GetLatestLogs() {
        File logsFile = new File("logs/latest.log");
        try {
            String logsContent = FileUtils.readFileToString(logsFile, StandardCharsets.UTF_8);
            return WebServer.OK(logsContent, null);
        } catch (IOException e) {
            return WebServer.InternalServerError("Failed to read server logs: " + e.getMessage(), null);
        }
    }

    public static Response HandlePUTRequest(IHTTPSession session) {
        try {
            Map<String, String> params = session.getParms();
            String command = params.get("command");

            if (command == null) {
                return WebServer.BadRequest("Please provide a command!",null);
            }
            
            try {
                boolean commandResult = Bukkit.getScheduler().callSyncMethod(McPanelX_Core.plugin, () -> {
                    return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }).get();

                if (commandResult) {
                    return WebServer.OK("Command executed successfully!",null);
                } else {
                    return WebServer.BadRequest("Failed to execute command!",null);
                }
            } catch (CommandException e) {
                return WebServer.BadRequest("Failed to execute command: " + e.getMessage(),null);
            }
        } catch (Exception e) {
            return WebServer.InternalServerError("Failed to execute command: " + e.getMessage(),null);
        }
    }
}
