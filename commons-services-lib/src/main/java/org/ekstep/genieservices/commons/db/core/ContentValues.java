package org.ekstep.genieservices.commons.db.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 4/17/2017.
 *
 * @author anil
 */
public class ContentValues {

    /**
     * Holds the actual values
     */
    private HashMap<String, Object> mValues;

    /**
     * Creates an empty set of values using the default initial size
     */
    public ContentValues() {
        // Choosing a default size of 8 based on analysis of typical
        // consumption by applications.
        mValues = new HashMap<String, Object>(8);
    }

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Object value) {
        mValues.put(key, value);
    }

    /**
     * Returns the number of values.
     *
     * @return the number of values
     */
    public int size() {
        return mValues.size();
    }

    /**
     * Remove a single value.
     *
     * @param key the name of the value to remove
     */
    public void remove(String key) {
        mValues.remove(key);
    }

    /**
     * Removes all values.
     */
    public void clear() {
        mValues.clear();
    }

    /**
     * Gets a value. Valid value types are {@link String}, {@link Boolean},
     * {@link Number}, and {@code byte[]} implementations.
     *
     * @param key the value to get
     * @return the data for the value, or {@code null} if the value is missing or if {@code null}
     * was previously added with the given {@code key}
     */
    public Object get(String key) {
        return mValues.get(key);
    }

    /**
     * Returns a set of all of the keys and values
     *
     * @return a set of all of the keys and values
     */
    public Set<Map.Entry<String, Object>> valueSet() {
        return mValues.entrySet();
    }

    /**
     * Returns a set of all of the keys
     *
     * @return a set of all of the keys
     */
    public Set<String> keySet() {
        return mValues.keySet();
    }
}
