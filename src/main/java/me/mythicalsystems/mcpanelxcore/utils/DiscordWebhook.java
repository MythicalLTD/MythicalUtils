package me.mythicalsystems.mcpanelxcore.utils;

import me.mythicalsystems.mcpanelxcore.McPanelX_Core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class DiscordWebhook {
    

    public static void onPluginEnable() {
        String webhook = McPanelX_Core.config.getString("Discord.webhook");
        DiscordWebhook.sendMessage(webhook, "McPanelX Core has been enabled. (Private: "+getServerIP()+"|Public: "+getPublicIP()+")");
    }

    public static void onPluginDisable() {
        String webhook = McPanelX_Core.config.getString("Discord.webhook");
        DiscordWebhook.sendMessage(webhook, "McPanelX Core has been disabled. ("+getPublicIP()+")");
    }

    public static String getServerIP() {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            return localAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static String getPublicIP() {
        try {
            URL url = new URL("https://api.ipify.org");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String publicIP = reader.readLine();
            reader.close();

            return publicIP;
        } catch (Exception e) {
            return null;
        }
    }

    public static void sendMessage(String webhook, String message) {
        try {
            URL url = new URL(webhook);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonPayload = "{\"content\":\"" + message + "\"}";

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonPayload.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

            } else {

            }
        } catch (Exception e) {

        }
    }
}
