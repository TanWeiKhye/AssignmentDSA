package adt;

public interface QueueInterface<T> {
	
    // Adds an element to the rear of the queue.
    void enqueue(T element);

    // Removes and returns the element at the front of the queue.
    T dequeue();

    // Returns the element at the front of the queue without removing it.
    T peek();

    // Checks if the queue is empty.
    boolean isEmpty();

    // Returns the number of elements in the queue.
    int size();
}
