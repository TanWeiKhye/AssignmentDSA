/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;

/**
 *
 * @author hongj
 */
public interface ArrayListInterface<T> {
    public boolean add(T newEntry);
    public boolean add(int position, T newEntry);
    public T get(int index); 
    public T remove(int position);
    public void clear();
    public int size();
    public T replace(int position, T newEntry);
    public T getEntry(int position);
    public boolean contains(T anEntry);
    public int getLength();
    public boolean isEmpty();
    public boolean isFull();
    T[] toArray(T[] a);
}
