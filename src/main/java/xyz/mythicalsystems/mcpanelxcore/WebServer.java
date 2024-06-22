package xyz.mythicalsystems.mcpanelxcore;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;
import xyz.mythicalsystems.mcpanelxcore.api.Console;
import xyz.mythicalsystems.mcpanelxcore.api.Index;
import xyz.mythicalsystems.mcpanelxcore.api.Power;

public class WebServer extends NanoHTTPD {
    public WebServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        if (uri.equals("/")) {
            return Index.Request();
        } else if (uri.equals("/console")) {
            if (verifyKey(session)) {
                return Console.Request(session);
            } else {
                return Unauthorized("Please provide a valid api key!", null);
            }
        } else if (uri.equals("/power")) {
            if (verifyKey(session)) {
                return Power.Request(session);
            } else {
                return Unauthorized("Please provide a valid api key!", null);
            }
        } else {
            return NotFound("Please check our docs!", null);
        }
    }

    /**
     * Verify the key and log the IP address
     * 
     * @param key
     * @param session
     * @return
     */
    public static boolean verifyKey(IHTTPSession session) {
        try {
            Boolean logToConsole = McPanelX_Core.cfg().getBoolean("Panel.log_to_console");
            String key = getAuthorizationHeader(session);
            String strKey = McPanelX_Core.cfg().getString("Panel.api_key");
            String ipAddress = session.getHeaders().get("http-client-ip");
            if (key == null) {
                if (logToConsole) {
                    McPanelX_Core.getInstance().getLogger().info("[McPanelX-Core] [" + ipAddress
                            + "] Tried to authorize with the plugin api but gave no key inside the headers!");
                }
                return false;
            } else {
                if (strKey.equals(key)) {
                    if (logToConsole) {
                        McPanelX_Core.getInstance().getLogger().info("[McPanelX-Core] [" + ipAddress
                                + "] Authorized with the plugin api using a valid API key!");
                    }
                    return true;
                } else {
                    if (logToConsole) {

                        McPanelX_Core.getInstance().getLogger()
                                .info("[McPanelX-Core] [" + ipAddress
                                        + "] Tried to authorize with the plugin api using the API key: " + key
                                        + " but it was denied due to the key being incorrect!");
                    }
                    return false;
                }
            }
        } catch (Exception e) {
            McPanelX_Core.getInstance().getLogger().info("[McPanelX-Core] Authentication error: " + e);
            return false;
        }
    }

    /**
     * Get the authorization key from header
     * 
     * @return String|null Return the content of the Authorization header if given!!
     */
    public static String getAuthorizationHeader(IHTTPSession session) {
        try {
            Map<String, String> headers = session.getHeaders();
            if (headers.containsKey("authorization")) {
                String authorizationHeader = headers.get("authorization");
                return authorizationHeader;
            } else {
                return null;
            }
        } catch (Exception e) {
            McPanelX_Core.getInstance().getLogger().info("[McPanelX-Core] Authentication error: " + e);
            return null;
        }
    }

    /**
     * Send a JSON response with the given code, error, message, success, and extra
     * content.
     * 
     * @param i
     * @param error
     * @param message
     * @param success
     * @param extraContent
     *
     * @return newFixedLengthResponse
     */
    @SuppressWarnings("unused")
    public static Response sendManualResponse(int i, String error, String message, Boolean success,
            Map<String, Object> extraContent) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("code", i);
            response.put("error", error);
            response.put("message", message);
            response.put("success", success);

            if (extraContent != null) {
                response.putAll(extraContent);
            }
            Response.Status status;
            String jsonResponse = new Gson().toJson(response);
            if (i == 200) {
                status = Response.Status.OK;
            } else if (i == 201) {
                status = Response.Status.CREATED;
            } else if (i == 204) {
                status = Response.Status.NO_CONTENT;
            } else if (i == 400) {
                status = Response.Status.BAD_REQUEST;
            } else if (i == 401) {
                status = Response.Status.UNAUTHORIZED;
            } else if (i == 403) {
                status = Response.Status.FORBIDDEN;
            } else if (i == 404) {
                status = Response.Status.NOT_FOUND;
            } else if (i == 405) {
                status = Response.Status.METHOD_NOT_ALLOWED;
            } else if (i == 500) {
                status = Response.Status.INTERNAL_ERROR;
            } else {
                status = Response.Status.INTERNAL_ERROR;
            }
            if (status != null) {
                return newFixedLengthResponse(status, "application/json", jsonResponse);
            } else {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json", jsonResponse);
            }
        } catch (Exception e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json",
                    "{\"code\": 500, \"error\": \"Internal Server Error\", \"message\": \"An internal server error occurred\\"
                            + e + "\", \"success\": false}");
        }
    }

    public static Response BadRequest(String message, Map<String, Object> extraContent) {
        return sendManualResponse(400,
                "The server cannot or will not process the request due to an apparent client error", message, false,
                extraContent);
    }

    public static Response Forbidden(String message, Map<String, Object> extraContent) {
        return sendManualResponse(403,
                "The request contained valid data and was understood by the server, but the server is refusing action",
                message, false, extraContent);
    }

    public static Response InternalServerError(String message, Map<String, Object> extraContent) {
        return sendManualResponse(500, "The server has encountered a situation it doesn't know how to handle", message,
                false, extraContent);
    }

    public static Response MethodNotAllowed(String message, Map<String, Object> extraContent) {
        return sendManualResponse(405, "A request method is not supported for the requested resource", message, false,
                extraContent);
    }

    public static Response NotFound(String message, Map<String, Object> extraContent) {
        return sendManualResponse(404, "The server can not find the requested resource", message, false, extraContent);
    }

    public static Response OK(String message, Map<String, Object> extraContent) {
        return sendManualResponse(200, "The request has succeeded", message, true, extraContent);
    }

    public static Response Unauthorized(String message, Map<String, Object> extraContent) {
        return sendManualResponse(401,
                "The request has not been applied because it lacks valid authentication credentials for the target resource",
                message, false, extraContent);
    }

    public static Response NoContent(String message, Map<String, Object> extraContent) {
        return sendManualResponse(204, "The server successfully processed the request and is not returning any content",
                message, true, extraContent);
    }

    public static Response Created(String message, Map<String, Object> extraContent) {
        return sendManualResponse(201, "The request has been fulfilled, resulting in the creation of a new resource",
                message, true, extraContent);
    }
}
