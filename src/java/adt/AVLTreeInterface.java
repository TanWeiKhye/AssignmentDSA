package adt;

public interface AVLTreeInterface<T> {
	
    // Inserts a new element into the tree, maintaining balance.
    void insert(T data);

    // Removes an element from the tree, rebalancing as needed.
    boolean delete(T data);

    // Searches for a specific element in the tree.
    boolean search(T data);

    // Finds and returns the minimum element in the tree.
    T findMin();

    // Finds and returns the maximum element in the tree.
    T findMax();

    // Returns the number of nodes in the tree.
    int size();

    // Checks if the tree is empty.
    boolean isEmpty();
}
