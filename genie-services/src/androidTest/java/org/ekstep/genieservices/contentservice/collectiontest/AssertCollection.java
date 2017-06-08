package org.ekstep.genieservices.contentservice.collectiontest;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestActivity;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.util.Map;

/**
 * Created by GoodWorkLabs on 08-09-2016.
 */
public class AssertCollection extends GenieServiceTestBase {

    public static final String VISIBILITY_PARENT = "parent";
    public static final String VISIBILITY_DEFAULT = "default";

    public static final String EMPTY_COLLECTION_ECAR_ID = "do_20045823";
    public static final String EMPTY_COLLECTION_NAME = "TestCollection2682016-01";
    public static final String COLLECTION_ECAR_ID = "do_30019820";
    public static final String COLLECTION_ECAR_NAME = "Times Tables 2 to 10";

    public static final String CHILD_C2_ID = "do_30013486";
    public static final String CHILD_C2_Name = "Multiplication - 2 Times Table";
    public static final String CHILD_C3_ID = "do_30013497";
    public static final String CHILD_C3_Name = "Multiplication - 3 Times Table";
    public static final String CHILD_C4_ID = "do_30013504";
    public static final String CHILD_C4_Name = "Multiplication - 4 Times Table";
    public static final String CHILD_C5_ID = "do_30013509";
    public static final String CHILD_C5_Name = "Multiplication - 5 Times Table";
    public static final String CHILD_C6_ID = "do_30013510";
    public static final String CHILD_C6_Name = "Multiplication - 6 Times Table";
    public static final String CHILD_C7_ID = "do_30013511";
    public static final String CHILD_C7_Name = "Multiplication - 7 Times Table";
    public static final String CHILD_C8_ID = "do_30013527";
    public static final String CHILD_C8_Name = "Multiplication - 8 Times Table";
    public static final String CHILD_C9_ID = "do_30013526";
    public static final String CHILD_C9_Name = "Multiplication - 9 Times Table";
    public static final String CHILD_C10_ID = "do_30013525";
    public static final String CHILD_C10_Name = "Multiplication - 10 Times Table";
    public static final String CHILD_C16_ID = "do_30014521";
    public static final String CHILD_C16_Name = "Multiplication - 16 Times Table";

    public static final String CHILD_CONTENT_ECAR_ID = "do_30013504";
    public static final String CHILD_CONTENT_NAME = "Multiplication - 4 Times Table";

    public static final String CHILD_CONTENT2_ECAR_ID = "do_30013486";
    public static final String CHILD_CONTENT_NAME2 = "Multiplication - 2 Times Table";
    public static final String CHILD_APK_CONTENT_ID_TFT = "air.edu.washington.cs.treefrog";
    public static final String CHILD_APK_CONTENT_ID_TAKE_OFF = "org.ekstep.delta";

    public static void verifyNoChildContentEntry(String identifier) {
        ContentModel content = GenieServiceDBHelper.findContent(identifier);
        Assert.assertNotNull(content);
        Assert.assertFalse(ContentHandler.hasChildren(content.getLocalData()));
    }

    public static void verifyCollectionEntryAndVisibility(String identifier, String visibility) {

        ContentModel content = GenieServiceDBHelper.findContent(identifier);
        Assert.assertNotNull(content);
        Assert.assertTrue(ContentHandler.hasChildren(content.getLocalData()));
        Assert.assertTrue(content.getVisibility().equalsIgnoreCase(visibility));
    }

    public static void verifyContentEntryAndVisibility(String identifier, String visibility) {

        ContentModel content = GenieServiceDBHelper.findContent(identifier);
        Assert.assertNotNull(content);
        Assert.assertTrue(content.getVisibility().equalsIgnoreCase(visibility));
    }

    public static void verifyContentVersionToBeUpdated(String identifier, double version, int refCount) {

        ContentModel content = GenieServiceDBHelper.findContent(identifier);
        Assert.assertNotNull(content);
        Assert.assertTrue(refCount == content.getRefCount());

        Map mapData = GsonUtil.fromJson(content.getLocalData(), Map.class);
        Double pkgVersion = (Double) mapData.get("pkgVersion");
        Assert.assertTrue(version == pkgVersion);
    }

    public static void verifyContentIsDeleted(String identifier, GenieServiceTestActivity activity, String contentPath) {

        Assert.assertFalse(activity.isFilePresent(contentPath));
        ContentModel content = GenieServiceDBHelper.findContent(identifier);
        Assert.assertNotNull(content.getLocalData());
    }
}
