# Maze Generator and Pathfinder using Kruskal and Dijkstra

This Final Java project generates mazes using a randomized Kruskal's algorithm and finds the shortest path through the maze using Dijkstra's algorithm. It utilizes a custom implementation of data structures, such as priority queues and disjoint sets, to illustrate algorithmic concepts. This project was developed as part of **CSE 373: Data Structures and Algorithms** at the University of Washington.

([Course Project Link](https://courses.cs.washington.edu/courses/cse373/25wi/projects/mazes/))

<table>
  <tr>
    <td>
      <img width="480" alt="Maze generated with Kruskal's algorithm" src="https://github.com/user-attachments/assets/73bdccee-143c-4f2e-bded-111d719e2afd" />
      <p align="center"><em>Maze generated with Kruskal's algorithm</em></p>
    </td>
    <td>
      <img width="480" alt="Shortest path computed using Dijkstra's algorithm" src="https://github.com/user-attachments/assets/c50dadda-e816-4020-84b5-3865b1a10590" />
      <p align="center"><em>Shortest path computed using Dijkstra's algorithm</em></p>
    </td>
  </tr>
</table>

---

## Project Structure

These files were designed to work within the broader course-provided framework, which includes GUI rendering and graph models. The work is **not runnable as a standalone application**, but contributes key components integrated into a pre-existing system. This repository contains five key Java classes developed or extended from starter code:

### Core Files

- **`ArrayHeapMinPQ.java`**  
  Originally used for the heap project. Implements a minimum priority queue using a binary heap stored in an array. Supports efficient `add`, `removeMin`, and `changePriority` operations.

- **`DijkstraShortestPathFinder.java`**  
  Implements Dijkstra's algorithm to compute the shortest path from a source node to a target node in a weighted graph with non-negative edge weights.

- **`UnionBySizeCompressingDisjointSets.java`**  
  A disjoint-set (union-find) structure with union-by-size and path compression optimizations. Used in Kruskal's algorithm to detect cycles efficiently.

- **`KruskalMinimumSpanningTreeFinder.java`**  
  Implements Kruskal’s algorithm to generate a minimum spanning tree (MST) from a given graph using the disjoint set structure. In this project, it’s used to carve a maze.

- **`KruskalMazeCarver.java`**  
  Applies Kruskal’s MST approach to remove walls from a grid-based maze structure until a fully connected acyclic maze is formed.

---

## Features

- **Maze Generation**  
  Generates mazes using a randomized version of Kruskal’s algorithm to ensure each cell is reachable and there are no loops.

- **Shortest Path Finder**  
  Dijkstra’s algorithm efficiently computes the shortest path from the top-left to bottom-right corner of the maze.

- **Custom Data Structures**  
  Includes student-implemented heap-based priority queues and disjoint-set structures with compression and size heuristics.

---

## Technologies Used

- Java (Generics, Collections Framework)
- Graph Data Structures
- Disjoint Set / Union-Find
- Priority Queues
- Dijkstra’s Algorithm
- Kruskal’s Algorithm

---

## Author

- Author: Yu-Chu Hsieh, Pedley J. Huang
- Course: CSE 373 - Data Structures and Algorithms
- Institution: University of Washington

---

## License

This project is for educational purposes only.
