package org.ekstep.genieproviders.telemetry;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.ekstep.genieproviders.BaseContentProvider;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.db.contract.TelemetryEntry;

/**
 * Created on 19/5/17.
 * shriharsh
 */

public abstract class AbstractTelemetryProvider extends BaseContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return String.format("vnd.android.cursor.item/%s.provider.telemetry", getPackageName());
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String eventString = values.getAsString(TelemetryEntry.COLUMN_NAME_EVENT);
        GenieResponse response = getService().getTelemetryService().saveTelemetry(eventString);
        if (response != null && response.getStatus()) {
            return uri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private String getCompletePath() {
        String TELEMETRY_PATH = "telemetry";
        return String.format("%s.%s", getPackageName(), TELEMETRY_PATH);
    }
}
