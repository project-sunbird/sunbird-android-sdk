package org.ekstep.genieproviders.language;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

public class LanguageUriHandlerFactory {
    public static List<LanguageTraversalRuleUriHandler> uriHandlers(String AUTHORITY,
                                                                    Context context,
                                                                    String selection) {
        return Arrays.asList(
                new LanguageTraversalRuleUriHandler(AUTHORITY, context, selection)
        );
    }
}
