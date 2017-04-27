package org.ekstep.genieservices;

/**
 * Created by swayangjit on 19/4/17.
 */

public interface ServiceConstants {
    String NO_DATA_FOUND = "";
    String SERVICE_ERROR = "";

     interface Partner {
         String KEY_PARTNER_ID = "partnerid";
         String SHARED_PREF_SESSION_KEY = "partnersessionid";
         String MISSING_PARTNER_ID = "MISSING_PARTNER_ID";
         String MISSING_PUBLIC_KEY = "MISSING_PUBLIC_KEY";
         String INVALID_RSA_PUBLIC_KEY = "INVALID_RSA_PUBLIC_KEY";
    }

    interface Event {
        String ERROR_INVALID_EVENT = "Invalid event";
        String ERROR_INVALID_JSON = "Invalid Json";
    }

    interface Tags {
        String KEY_GENIE_TAGS = "GENIE_TAGS";
    }
}
