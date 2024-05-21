package xyz.mythicalsystems.mcpanelxcore.api;

import fi.iki.elonen.NanoHTTPD.Response;
import xyz.mythicalsystems.mcpanelxcore.WebServer;

public class Index {
      public static Response Request() {
        return WebServer.OK("Hello, World!",null);
    }
}
