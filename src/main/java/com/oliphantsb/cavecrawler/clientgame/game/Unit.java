package com.oliphantsb.cavecrawler.clientgame.game;

import com.oliphantsb.cavecrawler.clientgame.Util;
import com.oliphantsb.cavecrawler.clientgame.enums.Direction;
import com.oliphantsb.cavecrawler.clientgame.enums.ItemType;
import com.oliphantsb.cavecrawler.clientgame.enums.UnitType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Unit {

    private UnitType type;
    private UnitStats stats;
    private int currenthp;
    private List<Item> inventory = new ArrayList<>();
    private Item equippedarmor = null;
    private Item equippedweapon = null;
    private BoardSquare square;
    private Direction dir;
    private JLabel icon = null;
    private int moveUnits = 0;


    public Unit(UnitType type, BoardSquare square) {
        this.type = type;
        stats = this.type.getStats();
        currenthp = stats.getHealth();
        this.square = square;
    }

    public boolean takeDamage(int damage) {
        damage = Math.max(1, damage - this.getDefense());
        currenthp -= damage;
        return currenthp <= 0;
    }

    public void move(BoardSquare newsquare) {
        assert newsquare.getUnit() == null;
        assert newsquare.isOpen();
        square.setUnit(null);
        newsquare.setUnit(this);
        square = newsquare;
    }

    public void pickUp(Item item) {
        getSquare().removeItem(item);
        inventory.add(item);
    }

    public void drop(Item item, BoardSquare sq) {
        sq.addItem(item);
        inventory.remove(item);
    }

    public void equipArmor(Item item) {
        if (item != null) {
            assert item.getItemType() == ItemType.ARMOR;
            assert inventory.contains(item);
        }
        equippedarmor = item;
    }

    public void equipWeapon(Item item) {
        if (item != null) {
            assert item.getItemType() == ItemType.WEAPON;
            assert inventory.contains(item);
        }
        equippedweapon = item;
    }

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public Direction getDirection() {
        return dir;
    }

    public void setDirection(Direction d) {
        dir = d;
    }

    public char getAscii(boolean direction) {
        if (direction) {
            return dir.getAscii();
        } else {
            return type.getAscii();
        }
    }

    public int getAttack() {
        return equippedweapon == null ? stats.getAttack() : equippedweapon.getItemValue() + stats.getAttack();
    }

    public int getDefense() {
        return equippedarmor == null ? stats.getArmor() : equippedarmor.getItemValue() + stats.getArmor();
    }

    public int getEquippedArmorValue() {
        return equippedarmor == null ? 0 : equippedarmor.getItemValue();
    }

    public int getEquippedWeaponValue() {
        return equippedweapon == null ? 0 : equippedweapon.getItemValue();
    }

    public BoardSquare getSquare() {
        return square;
    }

    public UnitType getType() {
        return type;
    }

    public String getName() {
        return type.getName();
    }

    public String getStatsString() {
        return "HP: " + currenthp + "/" + stats.getHealth() +
                ", Attack: " + getAttack() + ", Defense: " + getDefense() +
                ", Gold: " + getGold();
    }

    public int getGold() {
        int gold = 0;
        for (Item i : getInventory()) {
            if (i.getItemType() == ItemType.GOLD) {
                gold += i.getItemValue();
            }
        }
        return gold;
    }

    public List<Item> getWeapons() {
        List<Item> filteredList = new ArrayList<>();
        for (Item i : getInventory()) {
            if (i.getItemType() == ItemType.WEAPON) {
                filteredList.add(i);
            }
        }
        return filteredList;
    }

    public List<Item> getArmor() {
        List<Item> filteredList = new ArrayList<>();
        for (Item i : getInventory()) {
            if (i.getItemType() == ItemType.ARMOR) {
                filteredList.add(i);
            }
        }
        return filteredList;
    }

    public void initializeIcon(JPanel panel) {
        icon = new JLabel(Util.getIcon(getType().getFile()));
        icon.setBounds(getSquare().getCol() * Util.iconSize, getSquare().getRow() * Util.iconSize, Util.iconSize, Util.iconSize);
        panel.add(icon);
        updateIcon();
    }

    public void setZOrder(JPanel panel) {
        panel.setComponentZOrder(icon, 0);
    }

    public void updateIcon() {
        icon.setToolTipText(getType().getName() + ": " + getStatsString());
        if (currenthp > 0 && getSquare().isInView()) {
            icon.setBounds(getSquare().getCol() * Util.iconSize, getSquare().getRow() * Util.iconSize, Util.iconSize, Util.iconSize);
            icon.setVisible(true);
        } else {
            icon.setVisible(false);
        }
    }

    public void addTurn(int turnLength) {
        moveUnits += turnLength;
    }

    public void subtractMoveUnits() {
        assert moveUnits >= stats.getSpeed();
        moveUnits -= stats.getSpeed();
    }

    public int getMoveUnits() { return moveUnits; }

    public int getSpeed() { return stats.getSpeed(); }
}
