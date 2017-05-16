package org.ekstep.genieproviders.content;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

public class ContentUriHandlerFactory {
    public static List<ContentUriHandler> uriHandlers(String AUTHORITY,
                                                      Context context,
                                                      String selection, String[] selectionArgs) {
        return Arrays.asList(
                new ContentUriHandler(AUTHORITY, selectionArgs)
        );
    }
}
