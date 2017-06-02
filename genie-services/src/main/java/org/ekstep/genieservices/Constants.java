package org.ekstep.genieservices;

/**
 * Created by swayangjit on 19/4/17.
 */

public interface Constants {
    String SHARED_PREFERENCE_NAME = "org.ekstep.genieservices.preference_file";
    String TELEMETRY_VERSION = "2";

    interface Params {
        String VERSION_CODE = "VERSION_CODE";
        String VERSION_NAME = "VERSION_NAME";
        String API_BASE_URL = "API_BASE_URL";
        String API_PASS = "API_PASS";
        String API_USER = "API_USER";
        String GID = "GID";
        String LOGLEVEL = "LOGLEVEL";
    }

    interface MimeType {
        String APK_MIME_TYPE = "application/vnd.android.package-archive";
        String ECML_MIME_TYPE = "application/vnd.ekstep.ecml-archive";
    }
}
