/**
 * File: ArrayHeapMinPQ.java
 * --------------------------
 * This class implements a minimum priority queue (min-PQ) using a binary heap
 * stored in an ArrayList. It supports efficient retrieval, removal, and 
 * priority updates of items based on extrinsic (external) priority values.
 *
 * This implementation adheres to the ExtrinsicMinPQ interface, where each item
 * is explicitly associated with a priority independent of the item's identity.
 *
 * Core Features:
 * - add(item, priority): inserts a new item with the specified priority.
 * - removeMin(): removes and returns the item with the smallest priority.
 * - changePriority(item, newPriority): updates the priority of an existing item.
 * - peekMin(): retrieves, but does not remove, the item with the smallest priority.
 * - contains(item): checks if the item exists in the priority queue.
 * - size(): returns the number of items in the priority queue.
 *
 * This data structure is part of a larger maze generation and solving system.
 * Specifically, it is used internally by Dijkstra's algorithm to prioritize nodes
 * during shortest-path computation in a grid-based maze.
 *
 * Algorithmic Notes:
 * - Under the hood, the heap is 1-indexed (index 0 is a dummy node).
 * - All core operations (insertion, removal, update) run in O(log n) time.
 *
 * Edge Cases:
 * - Throws NoSuchElementException if peekMin() or removeMin() is called on an empty queue.
 * - Throws IllegalArgumentException if add() is called with an item that already exists in the queue.
 *
 * Author: Yu-Chu Hsieh, Pedley J. Huang
 * Course: CSE 373 - Data Structures and Algorithms
 * Institution: University of Washington
 */

package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Map;

/**
 * @see ExtrinsicMinPQ
 */

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    Map<T, Integer> positions;
    int endIndex;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        positions = new HashMap<>();
        PriorityNode<T> dummy = new PriorityNode<T>(null, 0.0);
        items.add(dummy);
        this.endIndex = START_INDEX - 1;
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
        positions.put(items.get(a).getItem(), a);
        positions.put(items.get(b).getItem(), b);
    }


    /**
     * Adds a new item to the priority queue with the given priority.
     * 
     * @param item     the item to be inserted
     * @param priority the priority associated with the item
     * @throws IllegalArgumentException if the item is already in the queue
     */
    @Override
    public void add(T item, double priority) {
        if (!contains(item)) {
            items.add(new PriorityNode<>(item, priority));
            endIndex++;
            positions.put(item, endIndex);
            int currIndex = endIndex;
            perUp(currIndex, currIndex / 2);
        } else {
            throw new IllegalArgumentException("Element already contained!");
        }

    }

    /**
     * Returns true if the given item exists in the priority queue.
     * 
     * @param item the item to check
     * @return true if the item is in the queue, false otherwise
     */
    @Override
    public boolean contains(T item) {
        return positions.containsKey(item);
    }

    /**
     * Returns a string representation of the entire heap including the dummy node at index 0.
     * Useful for debugging purposes. The order shown is the internal array-based representation.
     * 
     * @return a string listing each PriorityNode in the heap, one per line
     */
    public String toString() {
        String result = "";
        for (int i = 0; i < items.size(); i++) {
            result += items.get(i).toString() + "\n";
        }
        return  result;
    }

    /**
     * Returns the item with the smallest priority without removing it.
     * 
     * @return the item with the smallest priority
     * @throws NoSuchElementException if the queue is empty
     */
    @Override
    public T peekMin() {
        if (endIndex == START_INDEX - 1) {
            throw new NoSuchElementException("EMPTY!!!");
        }
        return items.get(START_INDEX).getItem();
    }

    /**
     * Returns the index of the given item in the heap.
     *
     * This method looks up the position of the item in the internal `positions` map,
     * which maps each item to its current index in the binary heap.
     *
     * @param item the item to look up
     * @return the index of the item if it exists in the heap, or -1 if not found
     */
    private int find(T item) {
        return positions.getOrDefault(item, -1);
    }

    /**
     * Moves the item at the given index down the heap until the heap property is restored.
     * This version uses precomputed left and right child indices to avoid recomputation.
     * 
     * @param currIndex the index of the item to be percolated down
     * @param left      the index of the left child
     * @param right     the index of the right child
     */
    private void perDown(int currIndex, int left, int right) {
        while (left <= endIndex) {
            int smallerChild = left;

            // if there's smaller right child
            if (right <= endIndex &&
                items.get(right).getPriority() < items.get(left).getPriority()) {
                smallerChild = right;
            }

            // if curr is not smaller than children, stop percolate down
            if (items.get(currIndex).getPriority() <= items.get(smallerChild).getPriority()) {
                break;
            }

            swap(currIndex, smallerChild);

            // continue percolating down
            currIndex = smallerChild;
            left = currIndex * 2;
            right = currIndex * 2 + 1;
        }
    }

    /**
     * Moves the item at the given index up the heap until the heap property is restored.
     * 
     * @param currIndex the index of the item to be percolated up
     * @param parent    the index of the item's parent
     */
    private void perUp(int currIndex, int parent) {
        if (parent == 0) {
            return;
        }
        while (items.get(currIndex).getPriority() < items.get(parent).getPriority()) {
            swap(currIndex, parent);
            currIndex = parent;
            parent = currIndex / 2;
            if (parent == 0) {
                return;
            }
        }
    }

    /**
     * Removes and returns the item with the smallest priority from the queue.
     * If the queue is empty, throws a NoSuchElementException.
     * 
     * This method removes the root of the heap, replaces it with the last item,
     * and restores the heap property via percolate-down.
     * 
     * @return the item with the smallest priority
     * @throws NoSuchElementException if the queue is empty
     */
    @Override
    public T removeMin() {
        if (endIndex == START_INDEX) {
            PriorityNode<T> toReturn = items.remove(endIndex);
            positions.remove(toReturn.getItem());
            endIndex--;
            return toReturn.getItem();
        } else if (endIndex < START_INDEX) {
            throw new NoSuchElementException("EMPTY!!!");
        } else {
            PriorityNode<T> toReturn = items.get(START_INDEX);
            items.set(START_INDEX, items.get(endIndex));
            items.remove(endIndex);
            positions.remove(toReturn.getItem());
            endIndex--;
            int currIndex = START_INDEX;
            int left = currIndex * 2;
            int right = currIndex * 2 + 1;
            perDown(currIndex, left, right);
            return toReturn.getItem();
        }
    }

    /**
     * Changes the priority of the given item to a new value.
     * Performs percolate-up or percolate-down as needed to restore heap order.
     * 
     * @param item     the item whose priority is to be changed
     * @param priority the new priority value
     * @throws NoSuchElementException if the item does not exist in the queue
     */
    @Override
    public void changePriority(T item, double priority) {
        int currIndex = find(item);
        if (currIndex == -1) {
            throw new NoSuchElementException("not found!");
        }
        items.get(currIndex).setPriority(priority);
        perUp(currIndex, currIndex / 2);
        perDown(currIndex, currIndex * 2, currIndex * 2 + 1);
    }

    /**
     * Returns the number of items currently in the priority queue.
     * 
     * @return the number of items in the queue
     */
    @Override
    public int size() {
        return endIndex - START_INDEX + 1;
    }
}
