package me.mythicalsystems.mcpanelxcore.handlers;

public class ProtocolVersionTranslator {
    public static String translateProtocolToString(int prot) {
        if (prot == 765 || prot == 764 || prot == 763) {
            return "1.20.X";
        } else if (prot == 762 || prot == 761 || prot == 760 || prot == 759) {
            return "1.19.X";
        } else if (prot == 758 || prot == 757) {
            return "1.18.X";
        } else if(prot == 756 || prot == 755) {
            return "1.17.x";
        } else if (prot == 754 || prot == 753 || prot == 751 || prot == 736 || prot == 735)  {
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
        }
        else {
            return "UNKNOWN";
        }

    }
}
