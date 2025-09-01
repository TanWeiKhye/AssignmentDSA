package adt;

public class LinkedQueue<T> implements QueueInterface<T> {
    private Node front, rear;
    private int size;
    
    private class Node {
        T data;
        Node next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    public LinkedQueue() {
        front = rear = null;
        size = 0;
    }
    
    @Override
    public void enqueue(T element) {
        Node newNode = new Node(element);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }
    
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }
    
    @Override
    public T getFront() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return front.data;
    }
    
    @Override
    public boolean isEmpty() {
        return front == null;
    }
    
    @Override
    public void clear() {
        front = rear = null;
        size = 0;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        if (index == 0) {
            return dequeue();
        }
        
        Node current = front;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        
        T removedData = current.next.data;
        current.next = current.next.next;
        
        if (current.next == null) {
            rear = current;
        }
        
        size--;
        return removedData;
    }
}