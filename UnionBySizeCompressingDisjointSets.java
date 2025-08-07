/**
 * File: UnionBySizeCompressingDisjointSets.java
 * ----------------------------------------------
 * A generic implementation of the disjoint-set (union-find) data structure
 * using the quick-union by size strategy, with path compression optimization.
 * 
 * This structure efficiently supports dynamic connectivity queries, such as:
 * - Whether two elements belong to the same set (`findSet`)
 * - Merging two sets (`union`)
 * - Creating singleton sets (`makeSet`)
 *
 * Key Features:
 * - Union-by-size: always attaches the smaller tree to the root of the larger one
 *   to keep trees shallow and improve performance.
 * - Path compression: flattens the structure during find operations for near-constant-time queries.
 * - Generic item support via internal mapping to array indices (HashMap<T, Integer>).
 * - Backed by a single list of integer pointers where each element points to its parent or stores
 *   the negative size of its set (if it is a root).
 *
 * Time Complexity:
 * - Nearly constant amortized time (inverse Ackermann) for all operations
 *   when using both union-by-size and path compression.
 *
 * Applications:
 * - Network connectivity
 * - Kruskal’s algorithm for minimum spanning tree
 * - Image segmentation
 * - Dynamic equivalence relations
 *
 * Author: Yu-Chu Hsieh, Pedley J Huang
 * Course Project: CSE 373 – Data Structures and Algorithms
 * University of Washington
 */

package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    private HashMap<T, Integer> ids;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        ids = new HashMap<>();
    }

    /**
     * Creates a new singleton set containing the specified item.
     *
     * Each item is assigned a unique index in the internal pointer list,
     * and its pointer is initialized to -1, indicating it is the root
     * of a set of size 1.
     *
     * @param item the item to be placed into a new disjoint set
     * @throws IllegalArgumentException if the item is already in any set
     */
    @Override
    public void makeSet(T item) {
        if (ids.containsKey(item)) {
            throw new IllegalArgumentException("already contained in any sets");
        }
        pointers.add(-1);
        ids.put(item, pointers.size()-1);
    }

    /**
     * Finds and returns the index of the root representative for the set
     * containing the specified item.
     *
     * This method uses path compression to flatten the structure of the tree,
     * by making all nodes along the path from the item to the root point
     * directly to the root, improving future query times.
     *
     * @param item the item whose set representative is to be found
     * @return the index of the root representative of the set
     * @throws IllegalArgumentException if the item is not in any set
     */
    @Override
    public int findSet(T item) {
        if (!ids.containsKey(item)) {
            throw new IllegalArgumentException("not contained in any sets");
        }
        int index = ids.get(item);
        List<Integer> visited = new ArrayList<>();
        visited.add(index);
        while (pointers.get(index) >= 0) {
            index = pointers.get(index);
            visited.add(index);
        }
        for (int i = 0; i < visited.size(); i++) {
            if (pointers.get(visited.get(i)) > 0) {
                pointers.set(visited.get(i), index);
            }
        }
        return index;
    }

    /**
     * Merges the sets containing the two specified items, if they are disjoint.
     *
     * Uses the union-by-size heuristic to keep the tree shallow: the smaller
     * set's root is made a child of the larger set's root. Also updates the size
     * of the new root appropriately.
     *
     * If the two items already belong to the same set, no action is taken.
     *
     * @param item1 the first item
     * @param item2 the second item
     * @return true if the sets were merged successfully; false if they were already in the same set
     * @throws IllegalArgumentException if either item is not in any set
     */
    @Override
    public boolean union(T item1, T item2) {
        int parentIndex1 = findSet(item1);
        int parentIndex2 = findSet(item2);
        if (parentIndex2 == parentIndex1) {
            return false;
        }
        // int compressIndex = -1;
        if (pointers.get(parentIndex1) <= pointers.get(parentIndex2)) {
            // items1 is in a larger set
            pointers.set(parentIndex2, parentIndex1);
            pointers.set(parentIndex1, pointers.get(parentIndex1) - 1);
            return true;
        } else {
            pointers.set(parentIndex1, parentIndex2);
            pointers.set(parentIndex2, pointers.get(parentIndex2) - 1);
            return true;
        }

    }
}
