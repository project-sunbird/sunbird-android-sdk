package org.ekstep.genieservices;

/**
 * Created by swayangjit on 19/4/17.
 */

public interface ServiceConstants {
    String NO_DATA_FOUND = "NO_DATA_FOUND";
    String SERVICE_ERROR = "SERVICE_ERROR";
    String INVALID_JSON = "invalid json";
    String UNABLE_TO_CREATE_ANONYMOUS_USER = "unable to create anonymous user";
    String SUCCESS_RESPONSE = "successful";
    String FAILED_RESPONSE = "failed";
    String ERROR_DELETING_A_USER = "Error when deleting user profile";

    int ACCESS_STATUS_VIEWED = 1;
    String INVALID_USER = "INVALID_USER";
    String NO_USER_WITH_SPECIFIED_ID = "There is no user with specified id exists";
    String VALIDATION_ERROR = "VALIDATION_ERROR";
    String INVALID_PROFILE = "INVALID_PROFILE";
    String UNABLE_TO_FIND_PROFILE = "unable to find profile";

    String NOT_EXISTS = "NOT_EXISTS";
    String NO_CURRENT_USER = "There is no current user";

    /* Key Constants for all the key value store */
    String KEY_USER_SESSION = "session";

    String NEVER_SYNCED = "NEVER";
    String SYNC_PROMPT = "SYNC_PROMPT";

    interface PreferenceKey {
        //Sync service pref keys
        String SYNC_CONFIG_SHARED_PREFERENCE_KEY = "syncConfig";
        String LAST_SYNC_TIME = "lastSyncTime";
        String SYNC_FILE_SIZE = "SYNC_FILE_SIZE";
    }

    interface API {
        String LP_EXTENSION = "/%s/v2";
        String EP_EXTENSION = "/%s/v1";
        String ANALYTICS_EXTENSION = "/analytics";
    }

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

    interface APIExecutionMode {
        String MODE_WIFI = "WIFI";
        String MODE_MDATA = "MDATA";
        String MODE_LOCAL = "LOCAL";
        String MODE_NO_NETWORK = "NO_NETWORK";
    }
}
