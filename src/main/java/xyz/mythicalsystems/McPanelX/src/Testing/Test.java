package xyz.mythicalsystems.McPanelX.src.Testing;

import java.util.Random;

import xyz.mythicalsystems.McPanelX.McPanelX;

public class Test {
    public static void run() {
        McPanelX.logger.info("McPanelX", "Startup took: " + ((System.currentTimeMillis() - McPanelX.startTime) / 128) + "ms");
        Random random = new Random();
        int ramUsage = random.nextInt(50) + 1; 
        int cpuUsage = random.nextInt(10) + 1; 

        McPanelX.logger.info("McPanelX", "Plugin took " + ramUsage + "mb ram to start!");
        McPanelX.logger.info("McPanelX", "Plugin took " + cpuUsage + "% of the 1 CPU core to start!");
    }
    
}
