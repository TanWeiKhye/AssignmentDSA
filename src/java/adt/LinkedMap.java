package adt;

/**
 * LinkedMap - Custom Map implementation using a linked list
 * 
 * This class provides a custom implementation of the MapInterface using a singly linked list.
 */
public class LinkedMap<K, V> implements MapInterface<K, V> {
    private Node first; // Reference to the first node in the linked list
    private int size;   // Number of key-value mappings in this map

	@Override
	public V getOrDefault(K key, V defaultValue) {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}
    
    /**
     * Node class representing a key-value pair in the linked list
     */
    private class Node {
        K key;
        V value;
        Node next;
        
        /**
         * Constructs a new node with the specified key and value
         * 
         * @param key the key of the mapping
         * @param value the value of the mapping
         */
        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    
    /**
     * Constructs an empty LinkedMap
     */
    public LinkedMap() {
        first = null;
        size = 0;
    }
    
    @Override
    public void put(K key, V value) {
        Node current = first;
        
        // Check if key already exists - update value if found
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value; // Update existing key
                return;
            }
            current = current.next;
        }
        
        // Add new node at beginning (maintains O(1) insertion)
        Node newNode = new Node(key, value);
        newNode.next = first;
        first = newNode;
        size++;
    }
    
    @Override
    public V get(K key) {
        Node current = first;
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }
    
    @Override
    public boolean containsKey(K key) {
        Node current = first;
        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    
    @Override
    public void remove(K key) {
        if (first == null) return;
        
        // Check if first node is the one to remove
        if (first.key.equals(key)) {
            first = first.next;
            size--;
            return;
        }
        
        // Find the node to remove
        Node current = first;
        while (current.next != null) {
            if (current.next.key.equals(key)) {
                current.next = current.next.next;
                size--;
                return;
            }
            current = current.next;
        }
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return first == null;
    }
    
    @Override
    public void clear() {
        first = null;
        size = 0;
    }
    
    
    @Override
    public ArrayList<K> keySet() {
        ArrayList<K> keys = new ArrayList<>();
        Node current = first;
        
        while (current != null) {
            keys.add(current.key);
            current = current.next;
        }
        
        return keys;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        Node current = first;
        while (current != null) {
            sb.append(current.key).append("=").append(current.value);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        
        sb.append("}");
        return sb.toString();
    }
}