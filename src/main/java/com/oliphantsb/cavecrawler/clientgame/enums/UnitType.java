package com.oliphantsb.cavecrawler.clientgame.enums;

import com.oliphantsb.cavecrawler.clientgame.Util;
import com.oliphantsb.cavecrawler.clientgame.game.UnitStats;

public enum UnitType {
    PLAYER, ZOMBIE, SKELETON, SLUG, ERROR;

    public static UnitType fromAscii(char c) {
        switch (c) {
            case '@':
                return PLAYER;
            case 'Z':
                return ZOMBIE;
            case 'S':
                return SKELETON;
            case 'B':
                return SLUG;
            default:
                return ERROR;
        }
    }

    public char getAscii() {
        switch (this) {
            case PLAYER:
                return '@';
            case ZOMBIE:
                return 'Z';
            case SKELETON:
                return 'S';
            case SLUG:
                return 'B';
            default:
                return '*';
        }
    }

    public UnitStats getStats() {
        switch (this) {
            case PLAYER:
                return new UnitStats(5,20,1, Util.standardTurnLength);
            case ZOMBIE:
                return new UnitStats(2,5,1, Util.standardTurnLength * 2);
            case SKELETON:
                return new UnitStats(3, 5, 3, Util.standardTurnLength);
            case SLUG:
                return new UnitStats(1, 10, 2, Util.standardTurnLength * 3);
            default:
                return new UnitStats(0,0,0, 0);
        }
    }

    public String getName() {
        switch (this) {
            case PLAYER:
                return "you";
            case ZOMBIE:
                return "zombie";
            case SKELETON:
                return "skeleton";
            case SLUG:
                return "slug";
            default:
                return "error";
        }
    }

    public String getFile() {
        switch (this) {
            case PLAYER:
                return "heroicon.png";
            case ZOMBIE:
                return "zombieicon.png";
            case SKELETON:
                return "skeletonicon.png";
            case SLUG:
                return "slugicon.png";
            default:
                return "erroricon.png";
        }
    }
}