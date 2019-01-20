package com.oliphantsb.cavecrawler.clientgame.game;

import com.oliphantsb.cavecrawler.clientgame.Util;
import com.oliphantsb.cavecrawler.clientgame.enums.VisibilitySetting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GraphicalGame {

    private List<Unit> allunits = new ArrayList<>();
    private List<Unit> units = new ArrayList<>();
    private Board board = new Board();
    private Unit player;
    private long startTime = System.currentTimeMillis();
    private int numGold;
    private boolean notDead = true;
    private JFrame frame;
    private JLabel stats;
    private JPanel boardPanel;

    public GraphicalGame(String ip, String boardfile, JFrame frame) throws IOException {
        // TODO connecty things
        player = MapParser.parseMap(boardfile, units, board);
        allunits.addAll(units);
        allunits.add(player);
        numGold = board.getGold();
        this.frame = frame;
        assert player != null;
    }

    public void run(VisibilitySetting visibility) {
        frame.getContentPane().removeAll();
        setUpJFrame(visibility);
        Util.message(Util.s_graphical_help);
        for (Unit m : units) {
            GameLogic.equipMonster(m);
        }
        while (notDead) {
            if (numGold <= player.getGold() && player.getSquare().isEnd()) {
                Util.message(Util.s_win);
                break;
            }
            stats.setText(GameLogic.getStatsString(player, startTime));
            board.updateGraphical(numGold <= player.getGold(), allunits, boardPanel, player.getSquare());
            frame.repaint();
        }
        frame.requestFocus();
        frame.getContentPane().removeAll();
        Util.setJList(null, null);
        frame.removeKeyListener(frame.getKeyListeners()[0]);
    }

    private void setUpJFrame(VisibilitySetting visibility) {
        Container pane = frame.getContentPane();
        pane.removeAll();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        list.setFont(new Font(list.getFont().getName(), Font.PLAIN, Util.fontSize));
        Util.setJList(list, model);
        list.setFocusable(false);
        list.addListSelectionListener(e -> list.clearSelection());
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(board.getNumCols() * Util.iconSize, Util.topPartHeight));
        list.setToolTipText(Util.s_scroller_info);
        pane.add(listScroller);

        boardPanel = new JPanel();
        boardPanel.setLayout(null);
        boardPanel.setPreferredSize(new Dimension(board.getNumCols() * Util.iconSize,
                board.getNumRows() * Util.iconSize));
        boardPanel.setPreferredSize(new Dimension(board.getNumCols() * Util.iconSize, board.getNumRows() * Util.iconSize));
        boardPanel.setBackground(Color.BLACK);
        board.initializeGraphical(boardPanel, numGold <= player.getGold(), allunits, visibility);
        board.setZOrders(boardPanel, allunits);
        pane.add(boardPanel);

        stats = new JLabel(GameLogic.getStatsString(player, startTime));
        stats.setFont(new Font(stats.getFont().getName(), Font.PLAIN, Util.fontSize));
        stats.setForeground(Color.WHITE);
        stats.setToolTipText(Util.s_stats_info);
        pane.add(stats);

        frame.addKeyListener(new UserInputHandler());
        frame.pack();
        frame.setVisible(true);
        frame.requestFocus();
    }

    private class UserInputHandler implements KeyListener {

        boolean busy = false;
        private Map<Integer, String> map = Util.getKeyHandlerMap();

        public void keyTyped(KeyEvent e) { }

        public void keyPressed(KeyEvent e) { }

        public void keyReleased(KeyEvent e) {
            if (!busy) {
                busy = true;
                String command = keyToCommand(e);
                notDead = !GameLogic.handleCommand(command, board, player, units, null);
                frame.requestFocus();
                busy = false;
            }
        }

        private String keyToCommand(KeyEvent e) {
            return map.get(e.getKeyCode());
        }
    }
}
