package com.oliphantsb.cavecrawler.clientgame.game;

public class UnitStats {

    private int attack;
    private int health;
    private int armor;
    // higher speed is SLOWER
    private int speed;

    public UnitStats(int attack, int health, int armor, int speed) {
        this.attack = attack;
        this.health = health;
        this.armor = armor;
        this.speed = speed;
    }

    public int getAttack() { return attack; }

    public int getHealth() { return health; }

    public int getArmor() { return armor; }

    public int getSpeed() { return speed; }
}
