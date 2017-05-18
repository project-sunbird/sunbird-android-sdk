package org.ekstep.genieservices.content.utils;

import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

/**
 * Created on 5/18/2017.
 *
 * @author anil
 */
public class ContentUtil {

    private static final String TAG = ContentUtil.class.getSimpleName();

    private static final int INITIAL_VALUE_FOR_TRANSFER_COUNT = 0;

    public static int minCompatibilityLevel = 1;
    public static int maxCompatibilityLevel = 3;
    // TODO: 02-03-2017 : We can remove this later after few release
    public static int defaultCompatibilityLevel = 1;

    /**
     * Is compatible to current version of Genie.
     *
     * @return true if compatible else false.
     */
    public static boolean isCompatible(Double compatibilityLevel) {
        return (compatibilityLevel >= minCompatibilityLevel) && (compatibilityLevel <= maxCompatibilityLevel);
    }

    public static boolean isImportFileExist(ContentModel oldContentModel, ContentModel newContentModel) {
        if (oldContentModel == null || newContentModel == null) {
            return false;
        }

        boolean isExist = false;
        try {
            String oldIdentifier = oldContentModel.getIdentifier();
            String newIdentifier = newContentModel.getIdentifier();
            String oldVisibility = oldContentModel.getVisibility();
            String newVisibility = newContentModel.getVisibility();

            if (oldIdentifier.equalsIgnoreCase(newIdentifier) && oldVisibility.equalsIgnoreCase(newVisibility)) {
                isExist = oldContentModel.pkgVersion() >= newContentModel.pkgVersion();
            }
        } catch (Exception e) {
            Logger.e(TAG, "isImportFileExist", e);
        }

        return isExist;
    }

    /**
     * To Check whether the content is exist or not.
     *
     * @param oldContent Old ContentModel
     * @param newContent New ContentModel
     * @return True - if file exists, False- does not exists
     */
    public static boolean isContentExist(ContentModel oldContent, ContentModel newContent) {
        if (oldContent == null || newContent == null) {
            return false;
        }

        boolean isExist = false;
        try {
            String oldIdentifier = oldContent.getIdentifier();
            String newIdentifier = newContent.getIdentifier();
            if (oldIdentifier.equalsIgnoreCase(newIdentifier)) {
                isExist = (oldContent.pkgVersion() >= newContent.pkgVersion()) // If old content's pkgVersion is less than the new content then return false.
                        || oldContent.getContentState() == ContentConstants.State.ARTIFACT_AVAILABLE;  // If content_state is other than artifact available then also return  false.
            }
        } catch (Exception e) {
            Logger.e(TAG, "isContentExist", e);
        }

        return isExist;
    }

    private static boolean contentMetadataAbsent(Map<String, Object> localDataMap) {
        return localDataMap.get(ContentModel.KEY_CONTENT_METADATA) == null;
    }

    private static boolean contentMetadataPresentWithoutViralityMetadata(Map<String, Object> localDataMap) {
        return localDataMap.get(ContentModel.KEY_CONTENT_METADATA) != null
                && ((Map<String, Object>) localDataMap.get(ContentModel.KEY_CONTENT_METADATA)).get(ContentModel.KEY_VIRALITY_METADATA) == null;
    }

    private static boolean contentAndViralityMetadataPresent(Map<String, Object> localDataMap) {
        return localDataMap.get(ContentModel.KEY_CONTENT_METADATA) != null
                && ((Map<String, Object>) localDataMap.get(ContentModel.KEY_CONTENT_METADATA)).get(ContentModel.KEY_VIRALITY_METADATA) != null;
    }

    public static int transferCount(Map<String, Object> localDataMap) {
        try {
            return Double.valueOf(valueOf(viralityMetadata(localDataMap).get(ContentModel.KEY_TRANSFER_COUNT))).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    private static Map<String, Object> viralityMetadata(Map<String, Object> localDataMap) {
        return contentAndViralityMetadataPresent(localDataMap)
                ? (Map<String, Object>) ((Map<String, Object>) localDataMap.get(ContentModel.KEY_CONTENT_METADATA)).get(ContentModel.KEY_VIRALITY_METADATA)
                : new HashMap<String, Object>();
    }

    public static void addOrUpdateViralityMetadata(String localDataJson, String origin) {
        Map<String, Object> localDataMap = GsonUtil.fromJson(localDataJson, Map.class);

        if (contentMetadataAbsent(localDataMap)) {
            Map<String, Object> contentMetadata = new HashMap<>();
            localDataMap.put(ContentModel.KEY_CONTENT_METADATA, contentMetadata);
            Map<String, Object> viralityMetadata = new HashMap<>();
            contentMetadata.put(ContentModel.KEY_VIRALITY_METADATA, viralityMetadata);
            viralityMetadata.put(ContentModel.KEY_ORIGIN, origin);
            viralityMetadata.put(ContentModel.KEY_TRANSFER_COUNT, INITIAL_VALUE_FOR_TRANSFER_COUNT);
        } else if (contentMetadataPresentWithoutViralityMetadata(localDataMap)) {
            Map<String, Object> virality = new HashMap<>();
            ((Map<String, Object>) localDataMap.get(ContentModel.KEY_CONTENT_METADATA)).put(ContentModel.KEY_VIRALITY_METADATA, virality);
            virality.put(ContentModel.KEY_ORIGIN, origin);
            virality.put(ContentModel.KEY_TRANSFER_COUNT, INITIAL_VALUE_FOR_TRANSFER_COUNT);
        } else if (contentAndViralityMetadataPresent(localDataMap)) {
            Map<String, Object> viralityMetadata =
                    (Map<String, Object>) ((Map<String, Object>) localDataMap.get(ContentModel.KEY_CONTENT_METADATA)).get(ContentModel.KEY_VIRALITY_METADATA);
            viralityMetadata.put(ContentModel.KEY_TRANSFER_COUNT, transferCount(localDataMap) + 1);
        }
    }

}
