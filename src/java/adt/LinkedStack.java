package adt;

public class LinkedStack<T> implements StackInterface<T> {

	private Node<T> top;

	public LinkedStack() {
		this.top = null;
	}

	@Override
	public void push(T item) {
		Node<T> newNode = new Node<>(item);
		newNode.next = top;
		top = newNode;
	}

	@Override
	public T pop() {
		if (isEmpty()) {
			throw new java.util.EmptyStackException();
		}
		T data = top.data;
		top = top.next;
		return data;
	}

	@Override
	public T peek() {
		if (isEmpty()) {
			throw new java.util.EmptyStackException();
		}
		return top.data;
	}

	@Override
	public boolean isEmpty() {
		return top == null;
	}

	private class Node<T> {

		T data;
		Node<T> next;

		public Node(T data) {
			this.data = data;
			this.next = null;
		}
	}
}
