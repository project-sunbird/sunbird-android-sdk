package org.ekstep.genieproviders.content;

import android.content.Context;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieservices.GenieService;

import java.util.Arrays;
import java.util.List;

public class ContentUriHandlerFactory {
    public static List<IUriHandler> uriHandlers(String AUTHORITY,
                                                Context context,
                                                String selection, String[] selectionArgs, GenieService genieService) {
        return Arrays.asList(
                new ContentUriHandler(AUTHORITY, selectionArgs, genieService),
                new AllContentsUriHandler(AUTHORITY, selectionArgs, genieService),
                new FeedbackUriHandler(AUTHORITY,context, selection,selectionArgs, genieService),
                new RelatedContentUriHandler(AUTHORITY,context,selection,selectionArgs, genieService)
        );
    }
}
