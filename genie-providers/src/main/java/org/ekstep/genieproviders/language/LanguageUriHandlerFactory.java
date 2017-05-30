package org.ekstep.genieproviders.language;

import android.content.Context;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieservices.GenieService;

import java.util.Arrays;
import java.util.List;

public class LanguageUriHandlerFactory {
    public static List<IUriHandler> uriHandlers(String AUTHORITY,
                                                Context context,
                                                String selection, GenieService genieService) {
        return Arrays.asList(
                new LanguageTraversalRuleUriHandler(AUTHORITY, context, selection, genieService),
                new LanguageSearchUriHandler(AUTHORITY, context, selection, genieService)
        );
    }
}
