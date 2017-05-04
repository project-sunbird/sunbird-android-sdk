package org.ekstep.genieservices.commons.db.cache;

import java.util.Set;

/**
 * Created by swayangjit on 19/4/17.
 */

public interface IKeyValueStore {

    void putString(String key, String value);

    void putLong(String key, long value);

    void putBoolean(String key, boolean value);

    void putStringSet(String key, Set<String> value);

    String getString(String key, String defValue);

    long getLong(String key, long defValue);

    boolean getBoolean(String key, boolean defValue);

    Set<String> getStringSet(String key, Set<String> defValue);

    boolean contains(String key);

    void remove(String key);

    void clear();

}
