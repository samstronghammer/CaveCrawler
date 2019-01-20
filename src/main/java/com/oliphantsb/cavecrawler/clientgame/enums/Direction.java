package com.oliphantsb.cavecrawler.clientgame.enums;

public enum Direction {
    UP, DOWN, LEFT, RIGHT, ERROR;

    public char getAscii() {
        switch (this) {
            case UP:
                return '^';
            case DOWN:
                return 'v';
            case LEFT:
                return '<';
            case RIGHT:
                return '>';
            default:
                return '*';
        }
    }

    public static Direction fromAscii(char c) {
        switch (c) {
            case '^':
                return UP;
            case 'v':
                return DOWN;
            case '<':
                return LEFT;
            case '>':
                return RIGHT;
            default:
                return ERROR;
        }
    }

    public static Direction fromUser(char c) {
        switch (c) {
            case 'w':
                return UP;
            case 's':
                return DOWN;
            case 'a':
                return LEFT;
            case 'd':
                return RIGHT;
            default:
                return ERROR;
        }
    }

    public Direction getReverse() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return ERROR;
        }
    }
}
