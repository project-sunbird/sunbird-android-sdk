package org.ekstep.genieservices.commons.db.cache;

import java.util.Set;

/**
 * Created by swayangjit on 19/4/17.
 */

public interface IKeyValueStore {

    void putString(String key, String value);

    void putStringSet(String key,  Set<String> value);

    void putLong(String key, long value);

    void putBoolean(String key, boolean value);

    void putInt(String key, int value);

    String getString(String key, String defValue);

    long getLong(String key, long defValue);

    boolean getBoolean(String key, boolean defValue);

    Set<String> getStringSet(String key, Set<String> defValue);

    int getInt(String key, int defValue);

    boolean contains(String key);

    void remove(String key);

    void clear();

}
