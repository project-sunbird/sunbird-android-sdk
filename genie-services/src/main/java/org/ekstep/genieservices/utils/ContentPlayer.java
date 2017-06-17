package org.ekstep.genieservices.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import org.ekstep.genieservices.Constants;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentData;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.ReflectionUtil;

import java.util.Map;

/**
 * Created by swayangjit on 1/6/17.
 */

public class ContentPlayer {

    private static final String TAG = ContentPlayer.class.getSimpleName();
    private static final String GENIE_CANVAS_PACKAGE = "org.ekstep.geniecanvas";
    private static final String GENIE_QUIZ_APP_PACKAGE = "org.ekstep.quiz.app";
    private static final String GENIE_CANVAS_ACTIVITY = "org.ekstep.geniecanvas.MainActivity";
    private static ContentPlayer sContentPlayer;
    private String mQualifier;

    private ContentPlayer(String qualifier) {
        this.mQualifier = qualifier;
    }

    public static void init(String qualifier) {
        if (sContentPlayer == null) {
            sContentPlayer = new ContentPlayer(qualifier);
        }
    }

    public static void play(Context context, Content content, Map<String, Object> resourceBundle) {
        PackageManager manager = context.getPackageManager();
        ContentData contentData = content.getContentData();
        String osId = contentData.getOsId();
        if (sContentPlayer.mQualifier == null) {
            Toast.makeText(context, "App qualifier not found", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = null;
        if (content.getMimeType().equalsIgnoreCase(Constants.MimeType.APK_MIME_TYPE)) {
            if (ContentUtil.isAppInstalled(context, osId)) {
                intent = manager.getLaunchIntentForPackage(osId);
            } else {
                ContentUtil.openPlaystore(context, osId);
                return;
            }

        } else if (content.getMimeType().equalsIgnoreCase(Constants.MimeType.ECML_MIME_TYPE) && !isCollectionOrTextBook(content.getContentType())) {
            if (osId == null || GENIE_QUIZ_APP_PACKAGE.equals(osId) || GENIE_CANVAS_PACKAGE.equals(osId)) {
                Class<?> className = ReflectionUtil.getClass(GENIE_CANVAS_ACTIVITY);
                if (className == null) {
                    Toast.makeText(context, "Content player not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(context, className);
            }
        } else {
            Toast.makeText(context, "Content type not supported", Toast.LENGTH_SHORT).show();
            return;
        }

        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_ORIGIN, "Genie");

        if (content.getChildrenHierarchyInfo() != null) {
            intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_CONTENT_EXTRAS, GsonUtil.toJson(content.getChildrenHierarchyInfo()));
        }
        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_APP_INFO, GsonUtil.toJson(contentData));
        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_LANGUAGE_INFO, GsonUtil.toJson(resourceBundle));
        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_APP_QUALIFIER, sContentPlayer.mQualifier);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        context.startActivity(intent);

    }


    private static boolean isCollectionOrTextBook(String contentType) {
        return contentType.equalsIgnoreCase(ContentType.COLLECTION.getValue())
                || contentType.equalsIgnoreCase(ContentType.TEXTBOOK.getValue())
                || contentType.equalsIgnoreCase(ContentType.TEXTBOOK_UNIT.getValue());
    }
}
