package org.ekstep.genieservices.content;

/**
 * Created on 5/4/2017.
 *
 * @author anil
 */
public interface ContentConstants {

    String GENIE_EXTRACTED_ECAR_FOLDER_PATH = "/Ekstep/org.ekstep.genieservices";
    String ISO_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SZZZZZ";

    interface Visibility {
        String DEFAULT = "Default";
        String PARENT = "Parent";
    }

    interface Type {
        String GAME = "Game";
        String STORY = "Story";
        String WORKSHEET = "Worksheet";
        String COLLECTION = "collection";
        String TEXTBOOK = "textbook";
        String TEXTBOOK_UNIT = "textbookunit";
    }

    interface MimeType {
        String APPLICATION = "application/vnd.android.package-archive";
    }

    interface State {
        int SEEN_BUT_NOT_AVAILABLE = 0; // Seen but not available (only serverData will be available)
        int ONLY_SPINE = 1; // Only spine
        int ARTIFACT_AVAILABLE = 2; // Artifact available
    }

    interface AccessStatus {
        int IMPORTED_OR_NEW = 0;
        int VIEWED = 1;
        int FULLY_PLAYED = 2;
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