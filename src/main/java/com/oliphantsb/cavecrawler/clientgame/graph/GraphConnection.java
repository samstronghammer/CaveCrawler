package com.oliphantsb.cavecrawler.clientgame.graph;

/**
 * Represents a connection between two vertexes. Contains both vertexes and the
 * edge between them.
 *
 * @author Samuel Oliphant
 *
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public class GraphConnection<V extends Vertex<V, E>,
    E extends DirectedEdge<V, E>> {

  private V v0;
  private V v1;
  private E e;

  /**
   * Make connection between given vertices over the edge.
   *
   * @param vertex0
   *          first vertex
   * @param vertex1
   *          second vertex
   * @param edge
   *          connecting edge
   */
  public GraphConnection(V vertex0, V vertex1, E edge) {
    v0 = vertex0;
    v1 = vertex1;
    e = edge;
  }

  /**
   * @return the v0
   */
  public V getV0() {
    return v0;
  }

  /**
   * @return the v1
   */
  public V getV1() {
    return v1;
  }

  /**
   * @return the e
   */
  public E getE() {
    return e;
  }

}
