package com.oliphantsb.cavecrawler.clientgame.game;

import com.oliphantsb.cavecrawler.clientgame.Util;
import com.oliphantsb.cavecrawler.clientgame.enums.ItemType;

import javax.swing.*;

public class Item {

    private ItemType type;
    private int value;
    private String name;
    private JLabel icon;

    public Item(ItemType type, int value, String name) {
        this.type = type;
        this.value = value;
        this.name = name;
    }

    public char getAscii() {
        return type.getAscii();
    }

    public String toString() {
        if (type == ItemType.GOLD) {
            if (value == 1) {
                return "1 gold piece";
            } else {
                return Integer.toString(value) + " gold pieces";
            }
        } else {
            char modifier = value < 0 ? '-' : '+';
            return modifier + Integer.toString(value) + " " + name;
        }
    }

    public ItemType getItemType() {
        return type;
    }

    public int getItemValue() {
        return value;
    }

    public String getFile() {
        return ItemType.getFile(name);
    }

    public void initializeIcon(JPanel panel, BoardSquare square) {
        icon = new JLabel(Util.getIcon(getFile()));
        icon.setBounds(square.getCol() * Util.iconSize, square.getRow() * Util.iconSize, Util.iconSize, Util.iconSize);
        panel.add(icon);
        updateIcon(square, panel);
    }

    public void setZOrder(JPanel panel) {
        panel.setComponentZOrder(icon, 1);
    }

    public void updateIcon(BoardSquare square, JPanel panel) {
        icon.setToolTipText(toString());
        // TODO fails here with -1 arrayindexoutofbounds exception
        if (square != null && square.getItems().get(square.getItems().size() - 1) == this && square.getUnit() == null && square.isInView()) {
            icon.setBounds(square.getCol() * Util.iconSize, square.getRow() * Util.iconSize, Util.iconSize, Util.iconSize);
            icon.setVisible(true);
            setZOrder(panel);
        } else {
            icon.setVisible(false);
        }
    }
}
