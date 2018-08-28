package org.ekstep.genieproviders.group;

import android.content.Context;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieservices.GenieService;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 20/7/18.
 * shriharsh
 */
public class GroupUriHandlerFactory {
    public static List<IUriHandler> uriHandlers(String AUTHORITY,
                                                Context context,
                                                String selection, String[] selectionArgs, GenieService genieService) {
        return Arrays.asList(
                new SetGroupUriHandler(AUTHORITY, context, selection, selectionArgs, genieService),
                new CurrentGroupUriHandler(AUTHORITY, context, selection, selectionArgs, genieService)
        );
    }
}
