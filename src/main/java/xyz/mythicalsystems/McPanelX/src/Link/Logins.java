package xyz.mythicalsystems.McPanelX.src.Link;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

import xyz.mythicalsystems.McPanelX.McPanelX;

public class Logins {
    
    public static void add(UUID uuid, String ip, String brand_name, String client_version) {
        if (Account.doesUUIDExist(uuid)) {          
            try {
                
                Connection connection = McPanelX.connection;
                PreparedStatement statement = connection.prepareStatement("INSERT INTO mcpanelx_logins (uuid, ip, client_name, client_version) VALUES (?, ?, ?, ?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, ip);
                statement.setString(3, brand_name);
                statement.setString(4, client_version);
                statement.executeUpdate();
                statement.close();
                
            } catch (Exception e) {
                McPanelX.logger.error("Logins", "Error while tried to save the player info: " + e.getMessage());
            }
        } else {
            McPanelX.logger.error("Logins", "Error while tried to save the player info: UUID does not exist");
        }

    }
}
