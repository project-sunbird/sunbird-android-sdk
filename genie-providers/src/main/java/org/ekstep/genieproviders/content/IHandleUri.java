package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public interface IHandleUri {
    Cursor process();

    boolean canProcess(Uri uri);

    Uri insert(Uri uri, ContentValues values);
}
