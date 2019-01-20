package com.oliphantsb.cavecrawler.clientgame.game;

import com.oliphantsb.cavecrawler.clientgame.Util;
import com.oliphantsb.cavecrawler.clientgame.enums.Direction;
import com.oliphantsb.cavecrawler.clientgame.enums.ItemType;
import com.oliphantsb.cavecrawler.clientgame.enums.VisibilitySetting;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {

    private List<List<BoardSquare>> squares = new ArrayList<>();
    private VisibilitySetting visibility = null;

    public Board() {}

    public void addRow() { squares.add(new ArrayList<>()); }

    public BoardSquare addSquare(Unit unit, boolean open, boolean end, int row, int col) {
        BoardSquare sq = new BoardSquare(open, end, row, col);
        sq.setUnit(unit);
        return addSquare(sq);
    }

    public BoardSquare addSquare(BoardSquare sq) {
        squares.get(squares.size() - 1).add(sq);
        return sq;
    }

    public void initializeConnections() {
        for (int row = 0; row < squares.size(); row++) {
            for (int col = 0; col < squares.get(row).size(); col++) {
                BoardSquare sq = squares.get(row).get(col);
                if (sq.isOpen()) {
                    Set<BoardConnection> connections = new HashSet<>();
                    for (BoardSquare adj : adjacent(sq)) {
                        connections.add(new BoardConnection(sq, adj));
                    }
                    sq.setConnections(connections);
                } else {
                    sq.setConnections(new HashSet<>());
                }
            }
        }
    }

    private Set<BoardSquare> adjacent(BoardSquare sq) {
        Set<BoardSquare> adj = new HashSet<>();
        for (int deltar = -1; deltar <= 1; deltar ++) {
            for (int deltac = -1; deltac <= 1; deltac ++) {
                if (Math.abs(deltar) + Math.abs(deltac) == 1) {
                    BoardSquare a = get(sq.getRow() + deltar, sq.getCol() + deltac);
                    if (a != null) {
                        adj.add(a);
                    }
                }
            }
        }
        return adj;
    }

    private Set<BoardSquare> adjacent8(BoardSquare sq) {
        Set<BoardSquare> adj = new HashSet<>();
        for (int deltar = -1; deltar <= 1; deltar ++) {
            for (int deltac = -1; deltac <= 1; deltac ++) {
                if (deltar != 0 || deltac != 0) {
                    BoardSquare a = get(sq.getRow() + deltar, sq.getCol() + deltac);
                    if (a != null) {
                        adj.add(a);
                    }
                }
            }
        }
        return adj;
    }

    private BoardSquare get(int row, int col) {
        if (row >= 0 && row < squares.size() && col >= 0 && col < squares.get(row).size()) {
            return squares.get(row).get(col);
        } else {
            return null;
        }
    }

    public BoardSquare destination(Unit u, Direction d) {
        BoardSquare uSquare = u.getSquare();
        return destination(uSquare, d);
    }

    public BoardSquare destination(BoardSquare sq, Direction d) {
        switch (d) {
            case UP:
                return get(sq.getRow() - 1, sq.getCol());
            case DOWN:
                return get(sq.getRow() + 1, sq.getCol());
            case LEFT:
                return get(sq.getRow(), sq.getCol() - 1);
            case RIGHT:
                return get(sq.getRow(), sq.getCol() + 1);
            default:
                return sq;
        }
    }

    public void display(boolean directions, boolean hasGold, String stats, VisibilitySetting visibility, BoardSquare playerSquare) {
        for (int row = 0; row < squares.size(); row++) {
            for (int col = 0; col < squares.get(row).size(); col++) {
                BoardSquare sq = squares.get(row).get(col);
                if (sq.isOpen()) {
                    updateVisibility(playerSquare, sq);
                }
            }
        }
        //take care of walls.
        for (int row = 0; row < squares.size(); row++) {
            for (int col = 0; col < squares.get(row).size(); col++) {
                BoardSquare sq = squares.get(row).get(col);
                if (!sq.isOpen()) {
                    updateVisibility(playerSquare, sq);
                    boolean visible = false;
                    for (BoardSquare adj : adjacent8(sq)) {
                        if (adj.isInView() && adj.isOpen()) {
                            visible = true;
                            break;
                        }
                    }
                    if (visible) {
                        sq.see();
                    }
                }
            }
        }



        Util.message(stats);
        for (int row = 0; row < squares.size(); row++) {
            for (int col = 0; col < squares.get(row).size(); col++) {
                Util.printchar(squares.get(row).get(col).getAscii(directions, hasGold, visibility));
            }
            Util.printchar('\n');
        }
    }

    public void initializeGraphical(JPanel panel, boolean hasGold, List<Unit> allunits, VisibilitySetting visibility) {
        this.visibility = visibility;
        for (int row = 0; row < squares.size(); row++) {
            for (int col = 0; col < squares.get(row).size(); col++) {
                BoardSquare sq = squares.get(row).get(col);
                sq.initializeIcon(panel, hasGold);
                if (visibility == VisibilitySetting.MEDIUM) {
                    sq.see();
                    sq.unSee();
                }
                for (Item i : sq.getItems()) {
                    i.initializeIcon(panel, sq);
                }
            }
        }
        for (Unit u : allunits) {
            u.initializeIcon(panel);
        }
    }

    public void setZOrders(JPanel panel, List<Unit> allunits) {
        for (int row = 0; row < squares.size(); row++) {
            for (int col = 0; col < squares.get(row).size(); col++) {
                BoardSquare sq = squares.get(row).get(col);
                sq.setZOrder(panel);
                for (Item i : sq.getItems()) {
                    i.setZOrder(panel);
                }
            }
        }
        for (Unit u : allunits) {
            u.setZOrder(panel);
        }
    }

    public void updateGraphical(boolean hasGold, List<Unit> allunits, JPanel panel, BoardSquare playerSquare) {
        //take care of non-walls
        for (int row = 0; row < squares.size(); row++) {
            for (int col = 0; col < squares.get(row).size(); col++) {
                BoardSquare sq = squares.get(row).get(col);
                if (sq.isOpen()) {
                    updateVisibility(playerSquare, sq);
                    sq.updateIcon(hasGold);
                    for (Item i : sq.getItems()) {
                        i.updateIcon(sq, panel);
                    }
                }
            }
        }
        //take care of walls.
        for (int row = 0; row < squares.size(); row++) {
            for (int col = 0; col < squares.get(row).size(); col++) {
                BoardSquare sq = squares.get(row).get(col);
                if (!sq.isOpen()) {
                    updateVisibility(playerSquare, sq);
                    boolean visible = false;
                    for (BoardSquare adj : adjacent8(sq)) {
                        if (adj.isInView() && adj.isOpen()) {
                            visible = true;
                            break;
                        }
                    }
                    if (visible) {
                        sq.see();
                    }
                    sq.updateIcon(hasGold);
                }
            }
        }
        for (Unit u : allunits) {
            u.updateIcon();
            for (Item i : u.getInventory()) {
                i.updateIcon(null, panel);
            }
        }
    }

    public int getGold() {
        int gold = 0;
        for (List<BoardSquare> row : squares) {
            for (BoardSquare square : row) {
                for (Item i : square.getItems()) {
                    if (i.getItemType() == ItemType.GOLD) {
                        gold += i.getItemValue();
                    }
                }
            }
        }
        return gold;
    }

    public void addItem(Item i, int row, int col) {
        squares.get(row).get(col).addItem(i);
    }

    public int getNumRows() {
        return squares.size();
    }

    public int getNumCols() {
        int maxCols = 0;
        for (List<BoardSquare> list : squares) {
            maxCols = Math.max(list.size(), maxCols);
        }
        return maxCols;
    }

    public void updateVisibility(BoardSquare psq, BoardSquare osq) {
        if (visibility == VisibilitySetting.EASY) {
            osq.see();
        } else {
            boolean visible = true;
            int deltar = osq.getRow() - psq.getRow();
            int deltac = osq.getCol() - psq.getCol();
            int dr = osq.getRow() - psq.getRow() > 0 ? 1 : -1;
            int dc = osq.getCol() - psq.getCol() > 0 ? 1 : -1;
            int currRow = psq.getRow();
            int currCol = psq.getCol();
            while (currRow != osq.getRow() || currCol != osq.getCol()) {
                int rRatio = (Math.abs(deltac) + 1) * (Math.abs(currRow - psq.getRow()) + 1);
                int cRatio = (Math.abs(deltar) + 1) * (Math.abs(currCol - psq.getCol()) + 1);
                if (rRatio > cRatio) {
                    currCol += dc;
                } else if (rRatio < cRatio) {
                    currRow += dr;
                } else {
                    currRow += dr;
                    currCol += dc;
                }
                if (currRow != osq.getRow() || currCol != osq.getCol()) {
                    BoardSquare sq = get(currRow, currCol);
                    if (sq == null || !sq.isOpen()) {
                        visible = false;
                        break;
                    }
                }
            }
            if (visible) {
                osq.see();
            } else {
                osq.unSee();
            }
        }
    }
}
