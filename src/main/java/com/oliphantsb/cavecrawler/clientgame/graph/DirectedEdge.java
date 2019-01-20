package com.oliphantsb.cavecrawler.clientgame.graph;

/**
 * Interface for directed edges.
 *
 * @author Samuel Oliphant
 *
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public interface DirectedEdge<V extends Vertex<V, E>,
    E extends DirectedEdge<V, E>> {
  /**
   * @return the v0
   */
  V getV0();

  /**
   * @return the v1
   */
  V getV1();

  /**
   * @return the w
   */
  int getWeight();
}
