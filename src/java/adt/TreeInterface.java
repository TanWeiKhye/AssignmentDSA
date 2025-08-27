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
	
	  // Return the size
	  int size();
	
    Iterator<T> iterator();

    Iterator<T> preOrderIterator();

    Iterator<T> postOrderIterator();
    
    void clear(); 
}
