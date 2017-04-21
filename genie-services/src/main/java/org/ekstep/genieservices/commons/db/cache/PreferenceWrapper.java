package org.ekstep.genieservices.commons.db.cache;

import android.content.Context;
import android.content.SharedPreferences;

import org.ekstep.genieservices.commons.AndroidLogger;
import org.ekstep.genieservices.commons.AppContext;

/**
 * Created by swayangjit on 19/4/17.
 */

public class PreferenceWrapper implements IKeyValueStore {

    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mPrefsEditor;
    private AppContext mAppContext;


    public PreferenceWrapper(AppContext<Context,AndroidLogger> appContext,String name) {
        mAppContext=appContext;
        Context context=((Context) mAppContext.getContext()).getApplicationContext();
        mSharedPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mPrefsEditor = mSharedPrefs.edit();
    }

    @Override
    public void putString(String key, String value) {
        mPrefsEditor.putString(key, value);
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
    public long getLong(String key, long defValue) {
        return  mSharedPrefs.getLong(key, defValue);
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
