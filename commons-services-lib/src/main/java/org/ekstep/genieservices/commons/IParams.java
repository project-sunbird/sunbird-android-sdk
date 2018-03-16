package org.ekstep.genieservices.commons;

/**
 * Created on 27/4/17.
 *
 * @author swayangjit
 */
public interface IParams {

    /**
     * Adds a value to the set.
     *
     * @param key   the name of the value to put
     * @param value the data for the value to put
     */
    void put(String key, Object value);

    String getString(String key);

    long getLong(String key);

    int getInt(String key);

    boolean getBoolean(String key);

    boolean contains(String key);

    void remove(String key);

    void clear();

    interface Key {
        String APPLICATION_ID = "APPLICATION_ID";
        String VERSION_NAME = "VERSION_NAME";
        String PRODUCER_ID = "PRODUCER_ID";
        String CHANNEL_ID = "CHANNEL_ID";
        String TELEMETRY_BASE_URL = "TELEMETRY_BASE_URL";
        String LANGUAGE_PLATFORM_BASE_URL = "LANGUAGE_PLATFORM_BASE_URL";
        String TERMS_BASE_URL = "TERMS_BASE_URL";
        String CONFIG_BASE_URL = "CONFIG_BASE_URL";
        String SEARCH_BASE_URL = "SEARCH_BASE_URL";
        String CONTENT_LISTING_BASE_URL = "CONTENT_LISTING_BASE_URL";
        String CONTENT_BASE_URL = "CONTENT_BASE_URL";
        String APIGATEWAY_BASE_URL = "APIGATEWAY_BASE_URL";
        String USER_SERVICE_BASE_URL = "USER_SERVICE_BASE_URL";
        String ORG_SERVICE_BASE_URL = "ORG_SERVICE_BASE_URL";
        String COURSE_SERVICE_BASE_URL = "COURSE_SERVICE_BASE_URL";
        String CHANNEL_SERVICE_BASE_URL = "CHANNEL_SERVICE_BASE_URL";
        String FRAMEWORK_SERVICE_BASE_URL = "FRAMEWORK_SERVICE_BASE_URL";
        String API_PASS = "API_PASS";
        String API_USER = "API_USER";
        String MOBILE_APP_SECRET = "MOBILE_APP_SECRET";
        String MOBILE_APP_KEY = "MOBILE_APP_KEY";
        String MOBILE_APP_CONSUMER = "MOBILE_APP_CONSUMER";
        String LOG_LEVEL = "LOG_LEVEL";
        String MIN_COMPATIBILITY_LEVEL = "MIN_COMPATIBILITY_LEVEL";
        String MAX_COMPATIBILITY_LEVEL = "MAX_COMPATIBILITY_LEVEL";
        String PROFILE_PATH = "PROFILE_PATH";
        String NETWORK_CONNECT_TIMEOUT = "NETWORK_CONNECT_TIMEOUT";
        String NETWORK_READ_TIMEOUT = "NETWORK_READ_TIMEOUT";
        String OAUTH_SERVICE_IMPLEMENTATION = "OAUTH_SERVICE_IMPLEMENTATION";
    }
}
