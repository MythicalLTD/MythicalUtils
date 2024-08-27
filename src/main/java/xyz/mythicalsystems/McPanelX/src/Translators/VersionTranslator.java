package xyz.mythicalsystems.McPanelX.src.Translators;

import xyz.mythicalsystems.McPanelX.McPanelX;
import xyz.mythicalsystems.McPanelX.MinecraftPlugin;

public class VersionTranslator {
    /**
     * 
     * Translate the protocol version to a human readable version
     * 
     * @param prot
     * @return String
     * 
     */
    public static String translate(int prot) {
        if (prot > 768) {
            return "1.21.X +";
        }
        if (prot == 767) {
            return "1.21.X";
        }
        if (prot == 765 || prot == 764 || prot == 763 || prot == 764 || prot == 765 || prot == 766) {
            return "1.20.X";
        } else if (prot == 762 || prot == 761 || prot == 760 || prot == 759) {
            return "1.19.X";
        } else if (prot == 758 || prot == 757) {
            return "1.18.X";
        } else if (prot == 756 || prot == 755) {
            return "1.17.x";
        } else if (prot == 754 || prot == 753 || prot == 751 || prot == 736 || prot == 735) {
            return "1.16.x";
        } else if (prot == 578 || prot == 575 || prot == 573) {
            return "1.15.x";
        } else if (prot == 498 || prot == 490 || prot == 485 || prot == 480 || prot == 477) {
            return "1.14.x";
        } else if (prot == 404 || prot == 401 || prot == 393) {
            return "1.13.x";
        } else if (prot == 340 || prot == 338 || prot == 335) {
            return "1.12.x";
        } else if (prot == 316 || prot == 315) {
            return "1.11.x";
        } else if (prot == 210) {
            return "1.10.x";
        } else if (prot == 110 || prot == 109 || prot == 108 || prot == 107) {
            return "1.9.x";
        } else if (prot == 47) {
            return "1.8.x";
        } else if (prot == 5 || prot == 4) {
            return "1.7.x";
        } else {
            return "UNKNOWN";
        }
    }

    public static void TranslatePluginStartup() {

        if (MinecraftPlugin.version.contains("SNAPSHOT") || MinecraftPlugin.version.contains("PRE")) {
            McPanelX.logger.warn("Main",
                    "You are running a pre-release build of McPanelX-Core. This version is not stable and may contain bugs.");
        }
        if (MinecraftPlugin.version.contains("BETA")) {
            McPanelX.logger.warn("Main",
                    "You are running a beta build of McPanelX-Core. This version is not stable and may contain bugs.");

        }
    }
}
