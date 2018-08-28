package org.ekstep.genieservices.commons.db.cache;

import java.util.Set;

/**
 * Created on 19/4/17.
 *
 * @author swayangjit
 */
public interface IKeyValueStore {

    String getString(String key, String defValue);

    void putString(String key, String value);

    Set<String> getStringSet(String key, Set<String> defValue);

    void putStringSet(String key, Set<String> value);

    long getLong(String key, long defValue);

    void putLong(String key, long value);

    boolean getBoolean(String key, boolean defValue);

    void putBoolean(String key, boolean value);

    int getInt(String key, int defValue);

    void putInt(String key, int value);

    boolean contains(String key);

    void remove(String key);

    void clear();
}
