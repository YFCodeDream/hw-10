/**
 *
 * A class representing a map (or function) from keys of type
 * <code>K</code> to values of type <code>V</code>. A
 * <code>SimpleMap</code> represents a set of key-value pairs,
 * <code>(k, v)</code>. We call the <code>v</code> the value
 * associated with key <code>k</code>. The keys are pair-wise
 * distinct. That is, if pairs <code>(k, v)</code> and <code>(j,
 * u)</code> appear in the map, we require that <code>k</code> and
 * <code>j</code> are not equal (semantically equivalent). We say that
 * a key <code>k</code> is <em>present</em> in the map if the map
 * contains a pair <code>(k, v)</code> for some value <code>v</code>.
 *
 * Functionality is provided to get the value associated to a given
 * key, add a key-value pair, or modify the value associated with a
 * key, remove a key value pair, and determine if a given key is
 * contained in the map.
 *
 */

public interface SimpleMap<K, V> {

    /**
     *
     * Get the size of (i.e., number of key-value pairs stored in) the
     * <code>SimpleMap</code>.
     *
     * @return the number of key-value pairs stored in the map
     */
    int size();

    /**
     * Determine if the map is empty (i.e., contains no key-value pairs).
     *
     * @return true if and only if the the map is empty
     */
    boolean isEmpty();

    /**
     *
     * Get the (unique) value associated with key <code>key</code>, or
     * <code>null</code> if <code>key</code> is not present in the
     * map.
     *
     * @param key the key to be searched
     * @param the value associated with key <code>key</code> in the
     * map, or <code>null</code> if <code>key</code> is not present
     *
     */
    V get(K key);

    /**
     *
     * Set the value associated to key <code>key</code> to be
     * <code>value</code> and return the previous value associated
     * with <code>key</code>, or <code>null</code> if <code>key</code>
     * was not previously present.
     * 
     * @param key the key whose value is to be assigned
     * @param value the value to be assigned to key <code>key</code>
     * @return the previous value associated to <code>key</code>, or
     * <code>null</code> if <code>key</code> was not previously present
     *
     */
    V put(K key, V value);

    /** 
     *
     * Remove a key and its associated value from the map, and return
    the associated value (or <code>null</code> if the key was not
    present).
     *
     * @param key the key to be removed
     * @return the value associated with <code>key</code>, or
     * <code>null</code> if <code>key</code> is not present 
     *
     */
    V remove(K key);

    /**
     * 
     * Determine if a key is contained in the map.
     *
     * @param key the key to be searched for
     * @return <code>true</code> if and only if <code>key</code>
     * appears in the map 
     *
     */
    boolean contains(K key);
}
