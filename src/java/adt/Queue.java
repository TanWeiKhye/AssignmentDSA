/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;


/**
 *
 * @author hongj
 */
public class Queue<T> implements QueueInterface<T> {
    private T[] array;
    private final static int frontIndex = 0;
    private int backIndex;
    private static final int DEFAULT_CAPACITY = 50;

    public Queue() {
        this(DEFAULT_CAPACITY);
    }

    public Queue(int initialCapacity) {
        array = (T[]) new Object[initialCapacity];
        backIndex = -1;
    }

    public void enqueue(T newEntry) {
        if (!isArrayFull()) {
            backIndex++;
            array[backIndex] = newEntry;
        }
    }

    public T getFront() {
        return isEmpty() ? null : array[frontIndex];
    }

    public T dequeue() {
        if (isEmpty()) return null;

        T front = array[frontIndex];

        for (int i = frontIndex; i < backIndex; ++i) {
            array[i] = array[i + 1];
        }

        array[backIndex] = null;
        backIndex--;

        return front;
    }

    public boolean isEmpty() {
        return frontIndex > backIndex;
    }

    public void clear() {
        for (int i = frontIndex; i <= backIndex; i++) {
            array[i] = null;
        }
        backIndex = -1;
    }

    private boolean isArrayFull() {
        return backIndex == array.length - 1;
    }

    @Override
    public int size() {
        return backIndex + 1;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index > backIndex) {
            return null;
        }

        T removed = array[index];

        for (int i = index; i < backIndex; i++) {
            array[i] = array[i + 1];
        }

        array[backIndex] = null;
        backIndex--;

        return removed;
    }
}

