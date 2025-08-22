/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;

/**
 *
 * @author hongj
 */
public class ArrayList<T> implements ListInterface<T> {
    private T[] list;
    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 25;
    
    public ArrayList() {
        list = (T[]) new Object[DEFAULT_CAPACITY];
        numberOfEntries = 0;
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int initialCapacity) {
        list = (T[]) new Object[initialCapacity];
        numberOfEntries = 0;
    }

    @Override
    public boolean add(T newEntry) {
        if (isFull()) {
            doubleCapacity();
        }
        list[numberOfEntries++] = newEntry;
        return true;
    }

    @Override
    public boolean add(int position, T newEntry) {
        if ((position >= 0) && (position <= numberOfEntries)) {
            if (isFull()) {
                doubleCapacity();
            }
            makeRoom(position);
            list[position] = newEntry;
            numberOfEntries++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public T remove(int position) {
        if ((position >= 0) && (position < numberOfEntries)) {
            T result = list[position];
            removeGap(position);
            numberOfEntries--;
            return result;
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < numberOfEntries; i++) {
            list[i] = null;
        }
        numberOfEntries = 0;
    }

    @Override
    public T replace(int position, T newEntry) {
        if ((position >= 0) && (position < numberOfEntries)) {
            T originalEntry = list[position];
            list[position] = newEntry;
            return originalEntry;
        } else {
            return null;
        }
    }

    @Override
    public T getEntry(int position) {
        if ((position >= 0) && (position < numberOfEntries)) {
            return list[position];
        } else {
            return null;
        }
    }

    @Override
    public boolean contains(T anEntry) {
        for (int i = 0; i < numberOfEntries; i++) {
            if (list[i].equals(anEntry)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getLength() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public boolean isFull() {
        return numberOfEntries == list.length;
    }
    
    private void doubleCapacity() {
        T[] oldList = list;
        int oldSize = oldList.length;

        list = (T[]) new Object[oldSize * 2];
        for (int i = 0; i < oldSize; i++) {
            list[i] = oldList[i];
        }
    }

    private void makeRoom(int position) {
        for (int i = numberOfEntries; i > position; i--) {
            list[i] = list[i - 1];
        }
    }

    private void removeGap(int position) {
        for (int i = position; i < numberOfEntries - 1; i++) {
            list[i] = list[i + 1];
        }
        list[numberOfEntries - 1] = null;
    }

    @Override
    public int size() {
        return numberOfEntries;
    }

    @Override
    public T[] toArray(T[] a) {
        if (a.length < numberOfEntries) {
            // Create a new array of the same type with exact size
            T[] newArray = (T[]) java.lang.reflect.Array.newInstance(
                a.getClass().getComponentType(), numberOfEntries);

            for (int i = 0; i < numberOfEntries; i++) {
                newArray[i] = list[i];
            }
            return newArray;
        }

        // Copy contents to the provided array
        for (int i = 0; i < numberOfEntries; i++) {
            a[i] = list[i];
        }

        if (a.length > numberOfEntries) {
            a[numberOfEntries] = null; // Optional: null-terminate if extra space
        }

        return a;
    }
    
    @Override
    public T get(int index) {
        return getEntry(index);
    }
    
}
