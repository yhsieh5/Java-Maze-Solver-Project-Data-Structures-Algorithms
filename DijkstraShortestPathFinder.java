/**
 * File: DijkstraShortestPathFinder.java
 * ----------------------------------------
 * A generic implementation of Dijkstra's shortest path algorithm on directed weighted graphs.
 *
 * This class extends SPTShortestPathFinder to compute the shortest path from a source vertex
 * to all other vertices in a graph using Dijkstra’s algorithm. The algorithm guarantees
 * optimal paths in graphs with non-negative edge weights.
 *
 * Key Features:
 * - Generic support for any graph structure extending Graph<V, E> with
 * BaseEdge<V, E>
 * - Efficient priority queue-based implementation for fast minimum-distance
 * updates - Supports tracing and reconstruction of the shortest path using a
 * parent edge map - Returns either a successful path or failure (if the
 * destination is unreachable)
 *
 * Use Cases: - Route planning and navigation - Network latency optimization -
 * Dependency resolution with cost minimization - Job scheduling with weighted
 * dependencies
 *
 * Technologies Used: - Java generics and inheritance - Custom extrinsic
 * priority queue interface (supports efficient priority updates) - Graph
 * abstraction with edge traversal logic
 *
 * Author: Yu-Chu Hsieh, Pedley J Huang
 * Course Project: CSE 373 – Data Structures and Algorithms
 * University of Washington
 */
package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.ArrayHeapMinPQ;
// import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 *
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
        extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        // return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    /**
     * Computes the shortest-path tree from a starting vertex using Dijkstra's
     * algorithm.
     *
     * This method maintains a distance table and a priority queue to explore
     * vertices in order of increasing distance from the start. For each visited
     * vertex, it relaxes the edges to its neighbors, updating their tentative
     * distances and predecessor edges when a shorter path is found.
     *
     * Once a vertex's minimum distance is finalized (i.e., when it's removed
     * from the queue), it is never updated again, ensuring optimality in graphs
     * with non-negative weights.
     *
     * @param graph the input graph containing vertices and weighted edges
     * @param start the starting vertex from which to compute shortest paths
     * @param end the optional destination vertex; used to early-exit the search
     * @return a map from each reachable vertex to the edge used to reach it in
     * the shortest-path tree. This map can be used to reconstruct paths.
     */
    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {

        Map<V, E> toReturn = new HashMap<>();
        Map<V, Double> distTable = new HashMap<>();
        ExtrinsicMinPQ<V> toProcess = this.createMinPQ();

        distTable.put(start, 0.0);
        toProcess.add(start, 0.0);

        while (!toProcess.isEmpty()) {
            V current = toProcess.removeMin();

            if (current.equals(end)) {
                break;
            }

            for (E edge : graph.outgoingEdgesFrom(current)) {
                V from = edge.from(); // aka the current vertex
                V to = edge.to(); // neighbouring vertex the current points to
                double newDist = distTable.get(from) + edge.weight();

                if (!distTable.containsKey(to) || newDist < distTable.get(to)) {
                    distTable.put(to, newDist);
                    toReturn.put(to, edge);

                    if (toProcess.contains(to)) {
                        toProcess.changePriority(to, newDist);
                    } else {
                        toProcess.add(to, newDist);
                    }
                }
            }
        }
        return toReturn;
    }

    /**
     * Reconstructs the shortest path from the start vertex to the end vertex
     * using a previously constructed shortest-path tree.
     *
     * This method traverses the map of parent edges (`spt`) in reverse—from the
     * end vertex back to the start—by following each vertex's predecessor edge.
     * The path is then reversed to produce a forward-ordered list of edges.
     *
     * Special Case: - If the start and end vertices are the same, this method
     * returns a {@code SingleVertex} path, provided the vertex exists in the
     * tree. - If the end vertex is unreachable (i.e., not in {@code spt}), the
     * method returns a {@code Failure} object representing the absence of a
     * path.
     *
     * @param spt the shortest-path tree as a map from vertex to incoming edge
     * @param start the starting vertex of the path
     * @param end the ending vertex of the path
     * @return a {@code ShortestPath.Success} if a valid path is found,
     * {@code ShortestPath.Failure} otherwise
     */
    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            // return new ShortestPath.SingleVertex<>(start);
            // Ensure the vertex exists in the shortest path tree before returning success
            if (spt.containsKey(start) || start.equals(end)) {
                return new ShortestPath.SingleVertex<>(start);
            }
            return new ShortestPath.Failure<>();
        }

        List<E> toReturn = new ArrayList<>();
        V currentV = end;

        while (!currentV.equals(start)) {
            E edge = spt.get(currentV);
            if (edge == null) {
                return new ShortestPath.Failure<>();
            }
            toReturn.add(edge);
            currentV = edge.from();
        }

        Collections.reverse(toReturn);

        return new ShortestPath.Success<>(toReturn);
    }

}
