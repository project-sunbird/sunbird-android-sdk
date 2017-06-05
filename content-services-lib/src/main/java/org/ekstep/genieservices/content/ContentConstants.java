package org.ekstep.genieservices.content;

import org.ekstep.genieservices.commons.utils.DateUtil;

/**
 * Created on 5/4/2017.
 *
 * @author anil
 */
public interface ContentConstants {

    String ISO_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SZZZZZ";

    long CACHE_TIMEOUT_HOME_CONTENT = (24 * DateUtil.MILLISECONDS_IN_AN_HOUR);

    String IMPORT_FAILED = "IMPORT_FAILED";
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

    interface MimeType {
        String APPLICATION = "application/vnd.android.package-archive";

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
        int FIRST_LEVEL_ALL = 0;
        /**
         * All descendant downloaded contents
         */
        int FIRST_LEVEL_DOWNLOADED = 1;
        /**
         * All descendant spine contents
         */
        int FIRST_LEVEL_SPINE = 2;

        int LEAF_LEVEL_ALL = 3;
        int LEAF_LEVEL_DOWNLOADED = 4;
        int LEAF_LEVEL_SPINE = 5;

//        int FIRST_LEVEL_TEXTBOOK_UNIT = 3;
    }

    interface Delete {
        int DEFAULT = 0;
        int NESTED = 1;
    }
}