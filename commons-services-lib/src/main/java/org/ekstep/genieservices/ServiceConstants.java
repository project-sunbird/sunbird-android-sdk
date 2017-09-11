package org.ekstep.genieservices;

/**
 * Created on 19/4/17.
 *
 * @author swayangjit
 */
public interface ServiceConstants {
    String UNABLE_TO_CREATE_ANONYMOUS_USER = "unable to create anonymous user";
    String SUCCESS_RESPONSE = "successful";
    String FAILED_RESPONSE = "failed";
    String ERROR_DELETING_A_USER = "Error when deleting user profile";
    String VALIDATION_ERROR = "VALIDATION_ERROR";

    /* Key Constants for all the key value store */
    String KEY_USER_SESSION = "session";

    String VERSION = "version";
    String EXPORT_ID = "export_id";
    String DID = "did";
    String EVENTS_COUNT = "events_count";
    String PROFILES_COUNT = "profiles_count";
    String EXPORT_TYPES = "types";
    String EXPORT_TYPE_TELEMETRY = "telemetry";
    String EXPORT_TYPE_PROFILE = "userprofile";
    String FILE_SIZE = "FILE_SIZE";
    String FILE_TYPE = "FILE_TYPE";
    String CONTENT_ITEMS_COUNT_KEY = "content_count";

    interface Params {
        String PROFILE_CONFIG = "PROFILE_CONFIG";
        String PLAYER_CONFIG = "PLAYER_CONFIG";
        String PARAMS = "PARAMS";
    }

    interface BundleKey {
        //ContentPlayer
        String BUNDLE_KEY_ORIGIN = "origin";
        String BUNDLE_KEY_MODE = "mode";
        String BUNDLE_KEY_CONTENT_EXTRAS = "contentExtras";
        String BUNDLE_KEY_APP_INFO = "appInfo";
        String BUNDLE_KEY_LANGUAGE_INFO = "languageInfo";
        String BUNDLE_KEY_APP_QUALIFIER = "appQualifier";

        String BUNDLE_KEY_DOWNLOAD_REQUEST = "download_request";
    }

    interface FileExtension {
        String CONTENT = "ecar";
        String PROFILE = "epar";
        String TELEMETRY = "gsa";
        String APK = "apk";
    }

    interface PreferenceKey {
        //Sync service pref keys
        String LAST_SYNC_TIME = "lastSyncTime";

        //Partner
        String KEY_ACTIVE_PARTNER_ID = "partner.activeid";
        String SHARED_PREF_SESSION_KEY = "partnersessionid";
        String SHARED_PREF_PARTNER_SET_EPOCH = "partnerSET";
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

    interface APIExecutionMode {
        String MODE_WIFI = "WIFI";
        String MODE_MDATA = "MDATA";
        String MODE_LOCAL = "LOCAL";
        String MODE_NO_NETWORK = "";
    }

    interface Telemetry {
        String CONTENT_IMPORT_INITIATED_SUB_TYPE = "ContentImport-Initiated";
        String CONTENT_IMPORT_SUCCESS_SUB_TYPE = "ContentImport-Success";
        String CONTENT_IMPORT_STAGE_ID = "ImportContent";
        String CONTENT_DOWNLOAD_INITIATE = "ContentDownload-Initiate";
        String CONTENT_DOWNLOAD_SUCCESS = "ContentDownload-Success";
        String CONTENT_DOWNLOAD_CANCEL = "ContentDownload-Cancel";
        String CONTENT_DETAIL = "ContentDetail";
    }

    interface ErrorCode {
        String PROCESSING_ERROR = "PROCESSING_ERROR";
        String DATA_NOT_FOUND_ERROR = "NO_DATA_FOUND";
        String VALIDATION_ERROR = "VALIDATION_ERROR";
        String DB_ERROR = "DB_ERROR";
        String INVALID_PROFILE = "INVALID_PROFILE";
        String PROFILE_NOT_FOUND = "PROFILE_NOT_FOUND";
        String INVALID_USER = "INVALID_USER";
        String EXPORT_FAILED = "EXPORT_FAILED";
        String IMPORT_FAILED = "IMPORT_FAILED";

        //Partner
        String UNREGISTERED_PARTNER = "UNREGISTERED_PARTNER";
        String CATASTROPHIC_FAILURE = "CATASTROPHIC_FAILURE";
        String ENCRYPTION_FAILURE = "ENCRYPTION_FAILURE";
        String MISSING_PARTNER_ID = "MISSING_PARTNER_ID";
        String MISSING_PUBLIC_KEY = "MISSING_PUBLIC_KEY";
        String INVALID_RSA_PUBLIC_KEY = "INVALID_RSA_PUBLIC_KEY";

        //Content
        String NO_DATA_FOUND = "NO_DATA_FOUND";
        String INVALID_FILE = "INVALID_FILE";
        String ECAR_NOT_FOUND = "ECAR_NOT_FOUND";

        // Notification
        String NO_NOTIFICATIONS_FOUND = "NO_NOTIFICATIONS_FOUND";
        String ADD_FAILED = "ADD_FAILED";
        String UPDATE_FAILED = "UPDATE_FAILED";
        String DELETE_FAILED = "DELETE_FAILED";

        // Authentication
        String TOKEN_GENERATION_FAILED = "TOKEN_GENERATION_FAILED";

        // KeyValueStore
        String KEY_NOT_FOUND = "KEY_NOT_FOUND";
    }

    interface ErrorMessage {
        String UNABLE_TO_FIND_PROFILE = "Unable to find profile";
        String UNABLE_TO_FIND_SUMMARY = "Unable to find summary";
        String UNABLE_TO_SAVE_LEARNER_ASSESSMENT = "unable to save learner assessment";
        String NO_USER_WITH_SPECIFIED_ID = "There is no user with specified id exists";

        String MANDATORY_FIELD_CONTENT_IDENTIFIER = "Content identifier could not be null or empty.";
        String MANDATORY_FIELD_UID = "Uid could not be null or empty.";
        String NO_FEEDBACK = "There is no feedback with specified uid and content identifier.";
        String NO_CONTENT_LISTING_DATA = "No data found.";

        String UNABLE_TO_CREATE_PROFILE = "Unable to create profile";
        String INVALID_PROFILE = "Invalid profile";
        String UNABLE_TO_FIND_ALL_PROFILE = "Unable to find all profile";
        String UNABLE_TO_UPDATE_PROFILE = "Unable to update profile";
        String UNABLE_TO_DELETE_PROFILE = "Unable to delete profile";
        String UNABLE_TO_SET_ANONYMOUS = "Unable to set anonymous profile";
        String UNABLE_TO_GET_ANONYMOUS = "Unable to get anonymous profile";
        String UNABLE_TO_SET_CURRENT_USER = "Unable to set current user";

        //Config
        String UNABLE_TO_FIND_MASTER_DATA = "Unable to find master data.";
        String UNABLE_TO_FIND_RESOURCE_BUNDLE = "Unable to find resource bundle.";
        String UNABLE_TO_FIND_ORDINALS = "Unable to find ordinals.";

        //Tag
        String TAG_NAME_SHOULD_NOT_BE_EMPTY = "Tag name can't be null or empty.";
        String UNABLE_TO_FIND_TAG = "Tag name not found.";

        //Sync
        String UNABLE_TO_SYNC = "Sync Failed.";

        //Telemetry
        String UNABLE_TO_SAVE_EVENT = "Not able to save event";

        //Import
        String IMPORT_PROFILE_FAILED = "Import profile failed.";
        String IMPORT_CONTENT_FAILED = "Import content failed.";
        String IMPORT_TELEMETRY_FAILED = "Import telemetry failed.";
        String FILE_DOES_NOT_EXIST = "Content import failed, file doesn't exist.";
        String UNSUPPORTED_FILE = "Content import failed, unsupported file extension";
        String CONTENT_NOT_FOUND = "No content found for identifier = ";
        String CONTENT_NOT_FOUND_TO_DELETE = "No content found to delete for identifier = ";
        String NO_CONTENT_TO_EXPORT = "Nothing to export!";

        String FAILED_TO_UPDATE_THE_NOTIFICATION = "Failed to update the notification";
        String FAILED_TO_DELETE_NOTIFICATION = "Failed to delete notification";
        String ERROR_WHILE_GETTING_NOTIFICATIONS = "Error while getting notifications";
        String FAILED_TO_ADD_UPDATE_NOTIFICATION = "Failed to add/update  notification";

        //Authentication
        String FAILED_TO_GENERATE_TOKEN = "Failed to generate the bearer token";

        //KeyStore Value
        String UNABLE_TO_FIND_KEY = "Unable to find key";
    }
}
