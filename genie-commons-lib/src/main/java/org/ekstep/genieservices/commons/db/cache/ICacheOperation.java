package org.ekstep.genieservices.commons.db.cache;

import java.util.Set;

/**
 * Created by swayangjit on 19/4/17.
 */

public interface ICacheOperation {

    void putString(String key, String value);

    void putLong(String key, long value);

    String getString(String key, String defValue);

    long getLong(String key, long defValue);

    boolean contains(String key);

    void remove(String key);

    void clear();


}
