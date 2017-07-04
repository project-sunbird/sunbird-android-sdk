package org.ekstep.genieservices.content;

import org.ekstep.genieservices.commons.utils.DateUtil;

/**
 * Created on 5/4/2017.
 *
 * @author anil
 */
public interface ContentConstants {

    String SUPPORTED_MANIFEST_VERSION = "1.1";
    String EKSTEP_CONTENT_ARCHIVE = "ekstep.content.archive";
    int TTL = 24;

    long CACHE_TIMEOUT_HOME_CONTENT = (24 * DateUtil.MILLISECONDS_IN_AN_HOUR);

    String IMPORT_FAILED_DEVICE_MEMORY_FULL = "IMPORT_FAILED_DEVICE_MEMORY_FULL";
    String NO_CONTENT_TO_IMPORT = "NO_CONTENT_TO_IMPORT";
    String UNSUPPORTED_MANIFEST = "UNSUPPORTED_MANIFEST";
    String DRAFT_ECAR_FILE_EXPIRED = "DRAFT_ECAR_FILE_EXPIRED";
    String IMPORT_FILE_EXIST = "IMPORT_FILE_EXIST";
    String ECAR_CLEANUP_FAILED = "ECAR_CLEANUP_FAILED";

    interface Visibility {
        String DEFAULT = "Default";
        String PARENT = "Parent";
    }

    interface ContentStatus {
        String LIVE = "Live";
        String DRAFT = "Draft";
    }

    interface MimeType {
        String APK = "application/vnd.android.package-archive";
        String ECML = "application/vnd.ekstep.ecml-archive";
        String HTML = "application/vnd.ekstep.html-archive";

        String ZIP = "application/zip";
        String TXT = "text/plain";
        String ECAR = "application/ecar";
        String EPAR = "application/epar";
    }

    interface State {
        int SEEN_BUT_NOT_AVAILABLE = 0; // Seen but not available (only serverData will be available)
        int ONLY_SPINE = 1; // Only spine
        int ARTIFACT_AVAILABLE = 2; // Artifact available
    }

    interface ChildContents {
        /**
         * Downloaded or spine both
         */
        int ALL = 0;
        /**
         * All descendant downloaded contents
         */
        int DOWNLOADED = 1;
        /**
         * All descendant spine contents
         */
        int SPINE = 2;
    }

}