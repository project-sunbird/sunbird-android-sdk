package org.ekstep.genieproviders.partner;

import android.content.Context;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieservices.GenieService;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 24/5/17.
 * shriharsh
 */

public class PartnerUriHandlerFactory {

    public static List<IUriHandler> uriHandlers(String AUTHORITY,
                                                Context context,
                                                String selection, String[] selectionArgs, GenieService genieService) {
        return Arrays.asList(
                new StartPartnerUriHandler(AUTHORITY, context, selection, selectionArgs, genieService),
                new EndPartnerUriHandler(AUTHORITY, context, selection, selectionArgs, genieService),
                new SendPartnerDataUriHandler(AUTHORITY, context, selection, selectionArgs, genieService)
        );
    }
}
