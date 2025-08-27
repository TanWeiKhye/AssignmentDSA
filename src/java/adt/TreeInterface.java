package adt;

public interface TreeInterface<T> {
	
    // Inserts a new element into the tree, maintaining balance.
    void insert(T data);

    // Removes an element from the tree, rebalancing as needed.
    void delete(T data);

    // Searches for a specific element in the tree.
    T search(T data);
    
    // Visit every node in chronologically or ascending order. (Left -> Root -> Right)
    void traverseInOrder();
    
    // Visit every node in order. (Root -> Left -> Right) [eg. Save or copy a tree]
    void traversePreOrder();
    
    // Visit every node in order. (Left -> Right -> Root) [eg. Delete/free a tree safely]
    void traversePostOrder();
    
    // Finds and returns the minimum element in the tree.
    T getMin();

    // Finds and returns the maximum element in the tree.
    T getMax();

	// Checks if the tree is empty.
    boolean isEmpty();
    
    void clear(); 
}
