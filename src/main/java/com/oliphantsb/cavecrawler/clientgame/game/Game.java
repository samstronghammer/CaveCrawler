package com.oliphantsb.cavecrawler.clientgame.game;

import com.oliphantsb.cavecrawler.clientgame.Util;
import com.oliphantsb.cavecrawler.clientgame.enums.VisibilitySetting;

import java.io.IOException;
import java.util.*;

public class Game {

    private List<Unit> units = new ArrayList<>();
    private Board board = new Board();
    private Unit player;

    private boolean showingDirections = false;
    private long startTime = System.currentTimeMillis();
    private int numGold;
    private Scanner sc;

    public Game(String ip, String boardfile, Scanner sc) throws IOException {
        // TODO connecty things
        this.sc = sc;
        player = MapParser.parseMap(boardfile, units, board);
        numGold = board.getGold();
        assert player != null;
    }

    public void run(VisibilitySetting visibility) {
        Util.message(Util.s_help);
        for (Unit m : units) {
            GameLogic.equipMonster(m);
        }
        board.display(false, numGold <= player.getGold(), GameLogic.getStatsString(player, startTime), visibility, player.getSquare());
        while (true) {
            if (numGold <= player.getGold() && player.getSquare().isEnd()) {
                Util.message(Util.s_win);
                break;
            }
            if (sc.hasNext()) {
                String next = sc.next();
                if (next.equals("t")) {
                    showingDirections = !showingDirections;
                } else {
                    if (GameLogic.handleCommand(next, board, player, units, sc)) {
                        break;
                    }
                }
                board.display(showingDirections, numGold <= player.getGold(), GameLogic.getStatsString(player, startTime), visibility, player.getSquare());
            }
        }
    }
}