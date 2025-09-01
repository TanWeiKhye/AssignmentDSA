package adt;

/**
 * MapInterface - Custom Map ADT interface
 * @author Your Name
 * 
 * This interface defines the basic operations for a Map ADT that stores key-value pairs.
 * It serves as an alternative to Java's Map interface from Collections Framework.
 */
public interface MapInterface<K, V> {
    
    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is replaced.
     * 
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     */
    void put(K key, V value);
    
    /**
     * Returns the value to which the specified key is mapped, or null if this map 
     * contains no mapping for the key.
     * 
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if no mapping exists
     */
    V get(K key);
    
    /**
     * Returns true if this map contains a mapping for the specified key.
     * 
     * @param key the key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified key
     */
    boolean containsKey(K key);
    
    /**
     * Removes the mapping for a key from this map if it is present.
     * 
     * @param key the key whose mapping is to be removed from the map
     */
    void remove(K key);
    
    /**
     * Returns the number of key-value mappings in this map.
     * 
     * @return the number of key-value mappings in this map
     */
    int size();
    
    /**
     * Returns true if this map contains no key-value mappings.
     * 
     * @return true if this map contains no key-value mappings
     */
    boolean isEmpty();
    
    /**
     * Removes all of the mappings from this map. The map will be empty after this call returns.
     */
    void clear();
    
    /**
     * Returns the value to which the specified key is mapped, or defaultValue if this map 
     * contains no mapping for the key.
     * 
     * @param key the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or defaultValue if no mapping exists
     */
    V getOrDefault(K key, V defaultValue);
    
    /**
     * Returns an ArrayList containing all of the keys in this map.
     * 
     * @return an ArrayList containing all the keys in this map
     */
    ArrayList<K> keySet();
}