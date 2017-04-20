package org.ekstep.genieservices.commons.db.cache;

/**
 * Created by swayangjit on 19/4/17.
 */

public interface IKeyValueOperation {

    void putString(String key, String value);

    void putLong(String key, long value);

    void putBoolean(String key, boolean value);

    String getString(String key, String defValue);

    long getLong(String key, long defValue);

    boolean getBoolean(String key, boolean defValue);

    boolean contains(String key);

    void remove(String key);

    void clear();


}
