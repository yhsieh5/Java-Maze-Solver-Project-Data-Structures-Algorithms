/**
 * File: KruskalMazeCarver.java
 * ----------------------------------------
 * Carves out a maze based on Kruskal's algorithm.
 * This class uses a minimum spanning tree approach to select which walls to remove
 * from a fully-walled maze, ensuring the resulting structure is a valid maze
 * with no cycles and full connectivity.
 * 
 * Author: Yu-Chu Hsieh, Pedley J Huang
 * Course Project: CSE 373 – Data Structures and Algorithms
 * University of Washington
 */

package mazes.logic.carvers;


import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;

import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(); // Uses system time
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed); // For deterministic randomness (e.g. testing)
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        // Hint: you'll probably need to include something like the following:
        // this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges));
        Set<EdgeWithData<Room, Wall>> setsOfWalls = new HashSet<>();

        for (Wall wall : walls) {
            Room from = wall.getRoom1();
            Room to = wall.getRoom2();
            EdgeWithData<Room, Wall> dataEdge = new EdgeWithData<>(from, to, this.rand.nextDouble(), wall);
            setsOfWalls.add(dataEdge);
        }

        MinimumSpanningTree<Room, EdgeWithData<Room, Wall>> result =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(setsOfWalls));

        Set<Wall> toRemove = new HashSet<>();
        for (EdgeWithData<Room, Wall> edge : result.edges()) {
            // extract the Wall object from EdgeWithData
            toRemove.add((Wall) edge.data());
        }

        return toRemove;
    }
}
