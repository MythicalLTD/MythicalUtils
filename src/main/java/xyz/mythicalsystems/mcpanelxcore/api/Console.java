package xyz.mythicalsystems.mcpanelxcore.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response;
import xyz.mythicalsystems.mcpanelxcore.McPanelX_Core;
import xyz.mythicalsystems.mcpanelxcore.WebServer;

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
        File logsFile = new File("proxy.log.0");
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
                return WebServer.BadRequest("Please provide a command!", null);
            }

            McPanelX_Core.getInstance().getProxy().getPluginManager()
                    .dispatchCommand(McPanelX_Core.getInstance().getProxy().getConsole(), command);
            return WebServer.OK("Command executed!", null);
        } catch (Exception e) {
            return WebServer.InternalServerError("Failed to execute command: " + e.getMessage(), null);
        }
    }
}
