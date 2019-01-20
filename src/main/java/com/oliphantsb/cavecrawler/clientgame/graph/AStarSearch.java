package com.oliphantsb.cavecrawler.clientgame.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Class for handling a dijkstra-like shortest-path search.
 *
 * @author Samuel Oliphant
 *
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public class AStarSearch<V extends Vertex<V, E>, E extends DirectedEdge<V, E>> {

  private static final int DEFAULT_PQ_SIZE = 11;
  private final V start;
  private final V finish;

  /**
   * Make a new search between the given vertices.
   *
   * @param startVertex
   *          start vertex
   * @param finishVertex
   *          goal vertex
   */
  public AStarSearch(V startVertex, V finishVertex) {
    start = startVertex;
    finish = finishVertex;
  }

  /**
   * Executes the shortest path algorithm on the stored start and finish
   * vertices.
   *
   * @return list of connections
   */
  public List<E> findSortestDistance() {
    List<E> connections = new ArrayList<>();
    Set<String> storedNodes = new HashSet<>();
    UcsNode node =
        new UcsNode(start, null, null, 0, start.getHeuristic(finish));
    CostPriorityQueue frontier = new CostPriorityQueue();
    frontier.add(node);
    while (frontier.size() > 0) {
      node = frontier.pop();
      if (node.getNode().getId().equals(finish.getId())) {
        while (node.getPrev() != null) {
          connections.add(0, node.getPrevEdge());
          node = node.getPrev();
        }
        return connections;
      }
      Set<E> edges = node.getNode().getOutgoingEdges();
      storedNodes.add(node.getNode().getId());
      for (E edge : edges) {
        assert edge.getV0().equals(node.getNode());
        V neighbor = edge.getV1();
        UcsNode neighborNode = new UcsNode(neighbor, edge, node,
            node.getCost() + edge.getWeight(), neighbor.getHeuristic(finish));
        if (!frontier.contains(neighbor.getId())
            && !storedNodes.contains(neighbor.getId())) {
          frontier.add(neighborNode);
        } else if (frontier.hasWorse(neighborNode)) {
          frontier.update(neighborNode);
        }
      }
    }
    return connections;
  }

  /**
   * Compares UcsNodes by cost.
   *
   * @author Samuel Oliphant
   *
   */
  private class CostComparer implements Comparator<UcsNode> {

    @Override
    public int compare(UcsNode n0, UcsNode n1) {
      return Double.compare(n0.getF(), n1.getF());
    }

  }

  /**
   * Tracks both a priority queue (sorted by cost), and a map from vertex ids to
   * their cost.
   *
   * @author Samuel Oliphant
   *
   */
  private class CostPriorityQueue {

    private PriorityQueue<UcsNode> pq =
        new PriorityQueue<>(DEFAULT_PQ_SIZE, new CostComparer());
    private Map<String, Double> costs = new HashMap<>();

    void add(UcsNode node) {
      pq.add(node);
      costs.put(node.getNode().getId(), node.getF());
    }

    UcsNode pop() {
      UcsNode node = pq.poll();
      costs.remove(node.getNode().getId());
      return node;
    }

    boolean contains(String id) {
      return costs.containsKey(id);
    }

    void update(UcsNode node) {
      pq.remove(node);
      pq.add(node);
      costs.put(node.getNode().getId(), node.getF());
    }

    boolean hasWorse(UcsNode node) {
      return costs.containsKey(node.getNode().getId())
          && node.getF() < costs.get(node.getNode().getId());
    }

    int size() {
      return pq.size();
    }
  }

  /**
   * Wrapper class for the vertexes in the graph that has a previous pointer and
   * current cost.
   *
   * @author Samuel Oliphant
   *
   */
  private class UcsNode {
    private V node;
    private E prevEdge;
    private double cost;
    private double heuristic;
    private UcsNode prev;

    UcsNode(V vertex, E previousEdge, UcsNode previous, double cost,
        double heuristic) {
      node = vertex;
      prevEdge = previousEdge;
      prev = previous;
      this.cost = cost;
      this.heuristic = heuristic;
    }

    /**
     * @return the node
     */
    public V getNode() {
      return node;
    }

    /**
     * @return the prevEdge
     */
    public E getPrevEdge() {
      return prevEdge;
    }

    /**
     * @return the a* function: cost + heuristic
     */
    public double getF() {
      return cost + heuristic;
    }

    /**
     * @return the cost
     */
    public double getCost() {
      return cost;
    }

    /**
     * @return the prev
     */
    public UcsNode getPrev() {
      return prev;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof AStarSearch<?, ?>.UcsNode) {
        return ((UcsNode) o).getNode().getId()
            .equals(this.getNode().getId());
      }
      return false;
    }

    @Override
    public int hashCode() {
      return getNode().getId().hashCode();
    }
  }
}
