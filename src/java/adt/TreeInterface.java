package adt;

import java.util.Iterator;

public interface TreeInterface<T> {

    // Inserts a new element into the tree, maintaining balance.
    void insert(T data);

    // Removes an element from the tree, rebalancing as needed.
    void delete(T data);

    // Searches for a specific element in the tree.
    T search(T data);
    
    // Finds and returns the minimum element in the tree.
    T getMin();

    // Finds and returns the maximum element in the tree.
    T getMax();

    // Checks if the tree is empty.
    boolean isEmpty();
    
    // Returns the number of elements in the tree
    int size();

    // Returns an iterator for in-order traversal
    Iterator<T> iterator();

    // Returns an iterator for pre-order traversal
    Iterator<T> preOrderIterator();

    // Returns an iterator for post-order traversal
    Iterator<T> postOrderIterator();
    
    // Clears the tree
    void clear();
    
    // Returns the element at the specified in-order index
    T get(int index);
}