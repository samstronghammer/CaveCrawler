package com.oliphantsb.cavecrawler.clientgame.enums;

public enum ItemType {
    WEAPON, ARMOR, GOLD, ERROR;

    public char getAscii() {
        switch (this) {
            case WEAPON:
                return '/';
            case ARMOR:
                return '%';
            case GOLD:
                return '$';
            default:
                return '*';
        }
    }

    public static ItemType fromAscii(char c) {
        switch (c) {
            case '/':
                return WEAPON;
            case '%':
                return ARMOR;
            case '$':
                return GOLD;
            default:
                return ERROR;
        }
    }

    public static String getFile(String name) {
        switch (name) {
            case "chain mail":
                return "chainmailicon.png";
            case "dagger":
                return "daggericon.png";
            case "gold":
                return "goldicon.png";
            case "leather shirt":
                return "leathershirticon.png";
            case "sword":
                return "swordicon.png";
            default:
                return "erroricon.png";
        }
    }
}
