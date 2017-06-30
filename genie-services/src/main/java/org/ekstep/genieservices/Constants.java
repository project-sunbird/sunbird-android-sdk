package org.ekstep.genieservices;

/**
 * Created on 19/4/17.
 *
 * @author swayangjit
 */
public interface Constants {
    String SHARED_PREFERENCE_NAME = "org.ekstep.genieservices.preference_file";

    interface Params {
        String VERSION_CODE = "VERSION_CODE";
        String VERSION_NAME = "VERSION_NAME";
        String API_BASE_URL = "API_BASE_URL";
        String API_PASS = "API_PASS";
        String API_USER = "API_USER";
        String GID = "GID";
        String LOGLEVEL = "LOGLEVEL";
        String APP_QUALIFIER = "QUALIFIER";
        String PROFILE_CONFIG = "PROFILE_CONFIG";
    }

    interface MimeType {
        String APK_MIME_TYPE = "application/vnd.android.package-archive";
        String ECML_MIME_TYPE = "application/vnd.ekstep.ecml-archive";
        String HTML_MIME_TYPE = "application/vnd.ekstep.html-archive";
    }
}
