package org.ekstep.genieproviders.content;

import android.content.Context;

import org.ekstep.genieproviders.IUriHandler;

import java.util.Arrays;
import java.util.List;

public class ContentUriHandlerFactory {
    public static List<IUriHandler> uriHandlers(String AUTHORITY,
                                                Context context,
                                                String selection, String[] selectionArgs) {
        return Arrays.asList(
                new ContentUriHandler(AUTHORITY, selectionArgs),
                new FeedbackUriHandler(AUTHORITY,context, selection,selectionArgs),
                new RelatedContentUriHandler(AUTHORITY,context,selection,selectionArgs)
        );
    }
}
