package org.ekstep.genieservices.config;

/**
 * Created on 5/10/2017.
 *
 * @author anil
 */
public interface ConfigConstants {

    interface ResourceFile {
        String MASTER_DATA_JSON_FILE = "terms.json";
        String RESOURCE_BUNDLE_JSON_FILE = "resource_bundle.json";
        String ORDINALS_JSON_FILE = "ordinals.json";
    }

    interface PreferenceKey {
        String RESOURCE_BUNDLE_API_EXPIRATION_KEY = "RESOURCE_BUNDLE_API_EXPIRATION_KEY";
        String MASTER_DATA_API_EXPIRATION_KEY = "TERMS_API_EXPIRATION_KEY";
        String ORDINAL_API_EXPIRATION_KEY = "ORDINAL_API_EXPIRATION_KEY";
    }
}
