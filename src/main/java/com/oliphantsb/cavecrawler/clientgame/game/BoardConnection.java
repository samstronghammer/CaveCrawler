package com.oliphantsb.cavecrawler.clientgame.game;

import com.oliphantsb.cavecrawler.clientgame.graph.DirectedEdge;

public class BoardConnection implements DirectedEdge<BoardSquare, BoardConnection> {

    private BoardSquare v0;
    private BoardSquare v1;

    public BoardConnection(BoardSquare v0, BoardSquare v1) {
        this.v0 = v0;
        this.v1 = v1;
    }

    @Override
    public BoardSquare getV0() {
        return v0;
    }

    @Override
    public BoardSquare getV1() {
        return v1;
    }

    @Override
    public int getWeight() {
        return v1.getUnit() == null ? 1 : 10;
    }
}
