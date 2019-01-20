package com.oliphantsb.cavecrawler.clientgame.graph;

import java.util.Set;

/**
 * Interface for vertex implementation.
 *
 * @author Samuel Oliphant
 *
 * @param <V>
 *          type of vertices
 * @param <E>
 *          type of edges
 */
public interface Vertex<V extends Vertex<V, E>, E extends DirectedEdge<V, E>> {
  /**
   * Return all outgoing edges.
   *
   * @return set of edges
   */
  Set<E> getOutgoingEdges();

  /**
   * Get vertex id. All vertices in a graph must have unique ids.
   *
   * @return vertex id
   */
  String getId();

  /**
   * Get the heuristic distance from another vertex.
   *
   * @param v
   *          Input vertex to get heuristic distance to.
   * @return heuristic distance
   */
  int getHeuristic(V v);
}
