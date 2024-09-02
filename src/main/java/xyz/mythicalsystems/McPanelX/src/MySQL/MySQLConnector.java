package xyz.mythicalsystems.McPanelX.src.MySQL;

import com.zaxxer.hikari.HikariDataSource;

import xyz.mythicalsystems.McPanelX.McPanelX;
import xyz.mythicalsystems.McPanelX.MinecraftPlugin;
import xyz.mythicalsystems.McPanelX.src.migrationm.MigrationM;
import xyz.mythicalsystems.McPanelX.src.migrationm.databasemanager.sql.SQLDatabaseManager;
import xyz.mythicalsystems.McPanelX.src.migrationm.util.ProgramInfo;

public class MySQLConnector {
    private HikariDataSource hikari;

    public MySQLConnector(String host, String port, String database, String username, String password) {
        hikari = new HikariDataSource();
        hikari.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikari.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikari.setUsername(username);
        hikari.setPassword(password);
        hikari.setMaximumPoolSize(10);
        hikari.setMinimumIdle(5);
        hikari.setConnectionTimeout(30000);
        hikari.setIdleTimeout(600000);
        hikari.setMaxLifetime(1800000000);
        hikari.setLeakDetectionThreshold(1800000000);
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    public void close() {
        hikari.close();
    }

    public void reconnect() {
        hikari.close();
        hikari = new HikariDataSource(hikari);
    }

    public void tryConnection() {
        try {

            hikari.getConnection().close();
            McPanelX.logger.info("MySQLConnector", "Connected to MySQL server");

            MigrationM migrationM = new MigrationM(new ProgramInfo(MinecraftPlugin.name, MinecraftPlugin.version, MinecraftPlugin.getInstance().getLogger()), new SQLDatabaseManager(hikari));
            migrationM.loadMigrations("migrations", MinecraftPlugin.class);
            boolean success = migrationM.migrate();
            if (success) {
                McPanelX.logger.info("MySQLConnector", "Migrations were successful");
            } else {
                McPanelX.logger.error("MySQLConnector", "Migrations failed");
                MinecraftPlugin.getInstance().getProxy().stop();
            }
            hikari.getConnection().close();

        } catch (Exception e) {
            McPanelX.logger.error("MySQLConnector", "Failed to connect to MySQL server: " + e.getMessage());
            MinecraftPlugin.getInstance().getProxy().stop();
        }
    }

}
