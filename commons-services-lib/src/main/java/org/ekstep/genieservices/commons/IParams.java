package org.ekstep.genieservices.commons;

/**
 * Created on 27/4/17.
 *
 * @author swayangjit
 */
public interface IParams {

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    void put(String key, Object value);

    Object get(String key);

    String getString(String key);

    long getLong(String key);

    int getInt(String key);

    boolean getBoolean(String key);

    boolean contains(String key);

    void remove(String key);

    void clear();
}
