package org.ekstep.genieservices.commons.db.cache;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created on 19/4/17.
 *
 * @author swayangjit
 */
public class PreferenceWrapper implements IKeyValueStore {

    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mPrefsEditor;

    public PreferenceWrapper(Context context, String name) {
        mSharedPrefs = context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        mPrefsEditor = mSharedPrefs.edit();
    }

    @Override
    public void putString(String key, String value) {
        mPrefsEditor.putString(key, value);
        mPrefsEditor.commit();
    }

    @Override
    public void putStringSet(String key, Set<String> value) {
        mPrefsEditor.putStringSet(key, value);
        mPrefsEditor.commit();
    }

    @Override
    public void putLong(String key, long value) {
        mPrefsEditor.putLong(key, value);
        mPrefsEditor.commit();
    }

    @Override
    public void putBoolean(String key, boolean value) {
        mPrefsEditor.putBoolean(key, value);
        mPrefsEditor.commit();
    }

    @Override
    public String getString(String key, String defValue) {
        return mSharedPrefs.getString(key, defValue);
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defValue) {
        return mSharedPrefs.getStringSet(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return mSharedPrefs.getLong(key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPrefs.getBoolean(key, defValue);
    }

    @Override
    public boolean contains(String key) {
        return mSharedPrefs.contains(key);
    }

    @Override
    public void remove(String key) {
        mPrefsEditor.remove(key);
        mPrefsEditor.commit();
    }

    @Override
    public void clear() {
        mPrefsEditor.clear();
        mPrefsEditor.commit();
    }

}
