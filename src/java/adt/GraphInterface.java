package adt;

import java.util.List;

public interface GraphInterface<T> {
	
    // Adds a vertex to the graph.
    void addVertex(T vertex);

    // Adds an edge between two vertices.
    void addEdge(T from, T to);

    // Removes a vertex and all its incident edges.
    void removeVertex(T vertex);

    // Removes an edge between two vertices.
    void removeEdge(T from, T to);

    // Checks if the graph contains a specific vertex.
    boolean hasVertex(T vertex);

    // Checks if an edge exists between two vertices.
    boolean hasEdge(T from, T to);

    // Returns a list of all vertices.
    List<T> getVertices();

    // Returns a list of all neighbors of a given vertex.
    List<T> getNeighbors(T vertex);

    // Performs a Breadth-First Search from a starting vertex.
    void breadthFirstSearch(T start);

    // Performs a Depth-First Search from a starting vertex.
    void depthFirstSearch(T start);
}
