package org.ekstep.genieproviders.language;

import android.content.Context;

import org.ekstep.genieproviders.IUriHandler;

import java.util.Arrays;
import java.util.List;

public class LanguageUriHandlerFactory {
    public static List<IUriHandler> uriHandlers(String AUTHORITY,
                                                Context context,
                                                String selection) {
        return Arrays.asList(
                new LanguageTraversalRuleUriHandler(AUTHORITY, context, selection),
                new GetAllLanguagesUriHandler(AUTHORITY, context, selection),
                new LanguageSearchUriHandler(AUTHORITY, context, selection)
        );
    }
}
