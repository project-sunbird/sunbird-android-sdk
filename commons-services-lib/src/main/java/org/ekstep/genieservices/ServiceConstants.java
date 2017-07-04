package org.ekstep.genieservices;

/**
 * Created on 19/4/17.
 *
 * @author swayangjit
 */
public interface ServiceConstants {
    String SERVICE_ERROR = "SERVICE_ERROR";
    String INVALID_JSON = "invalid json";
    String UNABLE_TO_CREATE_ANONYMOUS_USER = "unable to create anonymous user";
    String SUCCESS_RESPONSE = "successful";
    String FAILED_RESPONSE = "failed";
    String ERROR_DELETING_A_USER = "Error when deleting user profile";

    String INVALID_USER = "INVALID_USER";
    String NO_USER_WITH_SPECIFIED_ID = "There is no user with specified id exists";
    String VALIDATION_ERROR = "VALIDATION_ERROR";
    String INVALID_PROFILE = "INVALID_PROFILE";
    String UNABLE_TO_FIND_PROFILE = "unable to find profile";

    String NOT_EXISTS = "NOT_EXISTS";
    String NO_CURRENT_USER = "There is no current user";

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

    interface Params {
        String VERSION_CODE = "VERSION_CODE";
        String VERSION_NAME = "VERSION_NAME";
        String APP_QUALIFIER = "QUALIFIER";
        String TELEMETRY_BASE_URL = "TELEMETRY_BASE_URL";
        String LANGUAGE_PLATFORM_BASE_URL = "LANGUAGE_PLATFORM_BASE_URL";
        String LEARNING_PLATFORM_BASE_URL = "LEARNING_PLATFORM_BASE_URL";
        String CONFIG_BASE_URL = "CONFIG_BASE_URL";
        String SEARCH_BASE_URL = "SEARCH_BASE_URL";
        String CONTENT_LISTING_BASE_URL = "CONTENT_LISTING_BASE_URL";
        String ANALYTICS_BASE_URL = "ANALYTICS_BASE_URL";
        String API_PASS = "API_PASS";
        String API_USER = "API_USER";
        String GID = "GID";
        String LOG_LEVEL = "LOG_LEVEL";
        String MIN_COMPATIBILITY_LEVEL = "MIN_COMPATIBILITY_LEVEL";
        String MAX_COMPATIBILITY_LEVEL = "MAX_COMPATIBILITY_LEVEL";
        String PROFILE_CONFIG = "PROFILE_CONFIG";
        String PROFILE_PATH = "PROFILE_PATH";
    }

    interface BundleKey {
        //ContentPlayer
        String BUNDLE_KEY_ORIGIN = "origin";
        String BUNDLE_KEY_MODE = "mode";
        String BUNDLE_KEY_CONTENT_EXTRAS = "contentExtras";
        String BUNDLE_KEY_APP_INFO = "appInfo";
        String BUNDLE_KEY_LANGUAGE_INFO = "languageInfo";
        String BUNDLE_KEY_APP_QUALIFIER = "appQualifier";

        String BUNDLE_KEY_IS_CHILD = "is_child";
        String BUNDLE_KEY_LOCAL_FILE_PATH = "local_file_path";
        String BUNDLE_KEY_DESTINATION_FILE_PATH = "destination_file_path";
    }

    interface FileExtension {
        String CONTENT = "ecar";
        String PROFILE = "epar";
        String TELEMETRY = "gsa";
        String APK = "apk";
    }

    interface PreferenceKey {
        //Sync service pref keys
        String SYNC_CONFIG_SHARED_PREFERENCE_KEY = "syncConfig";
        String LAST_SYNC_TIME = "lastSyncTime";
        String SYNC_FILE_SIZE = "SYNC_FILE_SIZE";

        //Partner
        String KEY_PARTNER_ID = "partnerid";
        String KEY_ACTIVE_PARTNER_ID = "partner.activeid";
        String SHARED_PREF_SESSION_KEY = "partnersessionid";
        String SHARED_PREF_PARTNERSET_EPOCH = "partnerSET";
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

    }

    interface ErrorMessage {
        String UNABLE_TO_FIND_PROFILE = "unable to find profile";
        String UNABLE_TO_FIND_SUMMARY = "unable to find summary";
        String UNABLE_TO_SAVE_LEARNER_ASSESSMENT = "unable to save learner assessment";
        String NO_USER_WITH_SPECIFIED_ID = "There is no user with specified id exists";

        String MANDATORY_FIELD_CONTENT_IDENTIFIER = "Content identifier could not be null or empty.";
        String MANDATORY_FIELD_UID = "Uid could not be null or empty.";
        String NO_FEEDBACK = "There is no feedback with specified uid and content identifier.";
        String NO_CONTENT_LISTING_DATA = "No data found.";

        String UNABLE_TO_CREATE_PROFILE = "Unable to create profile";
        String INVALID_PROFILE = "Invalid profile";
        String UNABLE_TO_FIND_ALL_PROFILE = "Unable to find all profile";
        String UNABLE_TO_UPDTAE_PROFILE = "Unable to update profile";
        String UNABLE_TO_DELETE_PROFILE = "Unable to delete profile";
        String UNABLE_TO_SET_ANONYMOUS = "Unable to set anonymous profile";
        String UNABLE_TO_GET_ANONYMOUS = "Unable to get anonymous profile";
        String UNABLE_TO_SET_CURRENT_USER = "Unable to set current user";

        //Config
        String UNABLE_TO_FIND_MASTER_DATA = "Unable to find masterdata";
        String UNABLE_TO_FIND_RESOURCE_BUNDLE = "Unable to find resourcebundle";
        String UNABLE_TO_FIND_ORDINALS = "Unable to find ordinals";

        //Tag
        String TAG_NAME_SHOULD_NOT_BE_EMPTY = "Tag name can't be null or empty";
        String UNABLE_TO_FIND_TAG = "Tag name not found";

        //Sync
        String UNABLE_TO_SYNC = "Sync Failed";

        //Telemetry
        String UNABLE_TO_SAVE_EVENT = "Not able to save event";

        //Import
        String IMPORT_PROFILE_FAILED = "Import profile failed.";
        String IMPORT_CONTENT_FAILED = "Import content failed.";
        String IMPORT_TELEMETRY_FAILED = "Import telemetry failed.";
        String FILE_DOESNT_EXIST = "Content import failed, file doesn't exist.";
        String UNSUPPORTED_FILE = "Content import failed, unsupported file extension";
        String CONTENT_NOT_FOUND = "No content found for identifier = ";
        String CONTENT_NOT_FOUND_TO_DELETE = "No content found to delete for identifier = ";
        String NO_CONTENT_TO_EXPORT = "Nothing to export!";

        String FAILED_TO_UPDATE_THE_NOTIFICATION = "Failed to update the notification";
        String FAILED_TO_DELETE_NOTIFICATION = "Failed to delete notification";
        String ERROR_WHILE_GETTING_NOTIFICATIONS = "Error while getting notifications";
        String FAILED_TO_ADD_UPDATE_NOTIFICATION = "Failed to add/update  notification";
    }

    interface GeTransferEvent {
        String TRANSFER_DIRECTION_EXPORT = "EXPORT";
        String TRANSFER_DIRECTION_IMPORT = "IMPORT";
        String DATATYPE_TELEMETRY = "TELEMETRY";
        String DATATYPE_CONTENT = "CONTENT";
        String DATATYPE_EXPLODED_CONTENT = "EXPLODEDCONTENT";
        String DATATYPE_PROFILE = "PROFILE";
        String CONTENT_ITEMS_KEY = "contents";
        String FILE_SIZE = "FILE_SIZE";
        String FILE_TYPE = "FILE_TYPE";
    }
}
