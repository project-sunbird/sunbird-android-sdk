package org.ekstep.genieproviders;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public interface IUriHandler {
    Cursor process();

    boolean canProcess(Uri uri);

    Uri insert(Uri uri, ContentValues values);
}
