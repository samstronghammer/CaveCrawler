package com.oliphantsb.cavecrawler.clientgame.game;

import com.oliphantsb.cavecrawler.clientgame.enums.VisibilitySetting;
import com.oliphantsb.cavecrawler.clientgame.graph.Vertex;
import com.oliphantsb.cavecrawler.clientgame.Util;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BoardSquare implements Vertex<BoardSquare, BoardConnection> {

    private boolean open;
    private Unit unit = null;
    private List<Item> items = new ArrayList<>();
    private boolean end;
    private final int row;
    private final int col;
    private JLabel icon = null;
    private Set<BoardConnection> connections;
    private boolean seen = false;
    private boolean inView = false;
    private boolean hasGold = false;


    public BoardSquare(boolean open, boolean end, int row, int col) {
        assert open || !end; // Don't want end to be in a wall.
        this.open = open;
        this.end = end;
        this.row = row;
        this.col = col;
    }

    public void setConnections(Set<BoardConnection> connections) { this.connections = connections; }

    public Unit getUnit() { return unit; }

    public void setUnit(Unit unit) { this.unit = unit; }

    public List<Item> getItems() { return new ArrayList<>(items); }

    public void removeItem(Item i) { items.remove(i); }

    public void addItem(Item i) { items.add(i); }

    public boolean isOpen() { return open; }

    public boolean isEnd() { return end; }

    public boolean isSeen() { return seen; }

    public boolean isInView() { return inView; }

    public int getRow() { return row; }

    public int getCol() { return col; }

    public char getAscii(boolean direction, boolean hasGold, VisibilitySetting visibility) {
        if (seen || visibility != VisibilitySetting.HARD) {
            if (open) {
                if (getUnit() == null) {
                    if (items.size() == 0) {
                        if (end) {
                            if (hasGold) {
                                return 'O';
                            } else {
                                return '_';
                            }
                        } else {
                            return '.';
                        }
                    } else {
                        if (inView || visibility == VisibilitySetting.EASY) {
                            return items.get(items.size() - 1).getAscii();
                        } else {
                            return '.';
                        }
                    }
                } else {
                    if (inView || visibility == VisibilitySetting.EASY) {
                        return getUnit().getAscii(direction);
                    } else {
                        return '.';
                    }
                }
            } else {
                return '#';
            }
        } else {
            return ' ';
        }
    }

    public Icon getIcon(boolean hasGold, boolean inView) {
        if (open) {
            if (isEnd()) {
                if (hasGold) {
                    return inView ? Util.open_exit_icon : Util.dark_open_exit_icon;
                } else {
                    return inView ? Util.closed_exit_icon : Util.dark_closed_exit_icon;
                }
            } else {
                return inView ? Util.floor_icon : Util.dark_floor_icon;
            }
        } else {
            return inView ? Util.wall_icon : Util.dark_wall_icon;
        }
    }

    public void initializeIcon(JPanel panel, boolean hasGold) {
        icon = new JLabel(getIcon(hasGold, isInView()));
        icon.setBounds(getCol() * Util.iconSize, getRow() * Util.iconSize, Util.iconSize, Util.iconSize);
        panel.add(icon);
        updateIcon(hasGold);
    }

    public void setZOrder(JPanel panel) {
        panel.setComponentZOrder(icon, 2);
    }

    public void updateIcon(boolean hasGold) {
        if (isSeen()) {
            icon.setVisible(true);
            icon.setIcon(getIcon(hasGold, isInView()));
            if (isEnd()) {
                icon.setToolTipText(hasGold ? "Open exit!" : "Exit is closed. You need to collect all of the gold first.");
                if (hasGold && !this.hasGold) {
                    this.hasGold = true;
                    Util.message(Util.s_end_opens);
                }
            } else {
                icon.setToolTipText(isOpen() ? "Open space." : "Wall. Can't walk through here.");
            }

        } else {
            icon.setVisible(false);
        }
    }

    public void see() {
        seen = true;
        inView = true;
    }

    public void unSee() {
        inView = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BoardSquare) {
            BoardSquare other = (BoardSquare) o;
            return other.getRow() == getRow() && other.getCol() == getCol();
        }
        return false;
    }

    @Override
    public int hashCode() { return getId().hashCode(); }

    @Override
    public String getId() {
        return Integer.toString(getRow()) + ", " + Integer.toString(getCol());
    }

    @Override
    public Set<BoardConnection> getOutgoingEdges() {
        return connections;
    }

    @Override
    public int getHeuristic(BoardSquare sq) {
        return Math.abs(sq.getRow() - getRow()) + Math.abs(sq.getCol() + getCol());
    }
}
