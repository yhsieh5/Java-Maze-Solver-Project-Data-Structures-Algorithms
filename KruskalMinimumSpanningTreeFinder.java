/**
 * File: KruskalMinimumSpanningTreeFinder.java
 * ----------------------------------------
 * Finds a minimum spanning tree (MST) of the given undirected graph using Kruskal's algorithm.
 *
 * The method first initializes a disjoint set for all vertices in the graph, ensuring that each
 * vertex starts in its own set. Then, it sorts all edges in ascending order by weight.
 *
 * It iterates through each edge and checks whether its endpoints belong to different sets using
 * union-find. If they do, the edge is added to the MST and the sets are merged. This ensures no
 * cycles are formed and that the resulting tree spans all connected vertices.
 *
 * Special Cases:
 * - If the graph is empty (no vertices and no edges), returns an empty MST.
 * - If the graph has one vertex and no edges, returns an MST with a single node.
 * - If the graph is disconnected (i.e., cannot form a full spanning tree), returns a Failure.
 *
 * @param graph the undirected graph implementing KruskalGraph interface
 * @param <G> The type of graph being processed, must implement KruskalGraph
 * @param <V> The vertex type
 * @param <E> The edge type, must extend BaseEdge
 * @return a MinimumSpanningTree.Success with the edges of the MST if found, or
 * MinimumSpanningTree. Failure if no valid MST exists 
 * 
 * Author: Yu-Chu Hsieh, Pedley J Huang
 * Course Project: CSE 373 – Data Structures and Algorithms
 * University of Washington
 */

package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;

import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 *
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
        implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        // return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Special case: empty graph has a trivial MST
        if (graph.allVertices().isEmpty() && graph.allEdges().isEmpty()) {
            return new MinimumSpanningTree.Success<>();
        }
        // Special case: graph with one node and no edges also has a trivial MST
        else if (graph.allVertices().size() == 1 && graph.allEdges().isEmpty()) {
            return new MinimumSpanningTree.Success<>();
        }
        // Create a new disjoint set data structure to track connected components
        DisjointSets<V> verticeSet = new UnionBySizeCompressingDisjointSets<>();
        List<E> miniSpanTree = new ArrayList<>();

        // Initialize each vertex into its own disjoint set
        for (V vertex : graph.allVertices()) {
            verticeSet.makeSet(vertex);
        }

        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        // Iterate over edges in increasing weight order
        for (int i = 0; i <= edges.size() - 1; i++) {
            E currentEdge = edges.get(i);

            // If the two endpoints are in different sets, add edge to MST and merge sets
            if (verticeSet.union(currentEdge.to(), currentEdge.from())) {
                miniSpanTree.add(currentEdge);
            }

            // If we have V - 1 edges, the MST is complete
            if (miniSpanTree.size() == graph.allVertices().size() - 1) {
                return new MinimumSpanningTree.Success<>(miniSpanTree);
            }
        }
        return new MinimumSpanningTree.Failure<>();
    }
}
