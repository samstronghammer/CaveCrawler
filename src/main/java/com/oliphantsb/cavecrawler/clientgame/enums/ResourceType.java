package com.oliphantsb.cavecrawler.clientgame.enums;

public enum ResourceType {
    SPRITE, MAP, INFORMATION;

    public String getPrefix() {
        switch (this) {
            case SPRITE:
                return "Sprites/";
            case MAP:
                return "Maps/";
            case INFORMATION:
                return "Information/";
            default:
                return null;
        }
    }
}
