package org.ekstep.genieservices;

/**
 * Created by swayangjit on 19/4/17.
 */

public interface ServiceConstants {
    String NO_DATA_FOUND = "";
    String SERVICE_ERROR = "";
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

    interface PreferenceKey {
        String RESOURCE_BUNDLE_API_EXPIRATION_KEY = "RESOURCE_BUNDLE_API_EXPIRATION_KEY";
        String MASTER_DATA_API_EXPIRATION_KEY = "TERMS_API_EXPIRATION_KEY";
        String ORDINAL_API_EXPIRATION_KEY = "ORDINAL_API_EXPIRATION_KEY";
        String PARTNER_ID = "partnerid";
    }

    interface ConfigResourceFiles {
        String MASTER_DATA_JSON_FILE = "terms.json";
        String RESOURCE_BUNDLE_JSON_FILE = "resource_bundle.json";
        String ORDINALS_JSON_FILE = "ordinals.json";
    }
}
