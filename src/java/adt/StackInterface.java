package adt;

public interface StackInterface<T> {
	
    // Pushes an element onto the top of the stack.
    void push(T element);

    // Removes and returns the element at the top of the stack.
    T pop();

    // Returns the element at the top of the stack without removing it.
    T peek();

    // Checks if the stack is empty.
    boolean isEmpty();

    // Returns the number of elements in the stack.
    int size();
}
