package org.ekstep.genieservices.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentData;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.ReflectionUtil;
import org.ekstep.genieservices.content.ContentConstants;

import java.util.Map;

/**
 * Created on 1/6/17.
 *
 * @author swayangjit
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

        Intent intent;
        if (isApk(content.getMimeType())) {
            if (ContentUtil.isAppInstalled(context, osId)) {
                intent = manager.getLaunchIntentForPackage(osId);
            } else {
                ContentUtil.openPlaystore(context, osId);
                return;
            }

        } else if (isEcmlOrHtml(content.getMimeType())) {
            if (osId == null || GENIE_QUIZ_APP_PACKAGE.equals(osId) || GENIE_CANVAS_PACKAGE.equals(osId)) {
                Class<?> className = ReflectionUtil.getClass(GENIE_CANVAS_ACTIVITY);
                if (className == null) {
                    Toast.makeText(context, "Content player not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(context, className);
            } else {
                Toast.makeText(context, "Content player not found", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(context, "Content type not supported", Toast.LENGTH_SHORT).show();
            return;
        }

        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_ORIGIN, "Genie");
        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_MODE, "play");
        if (!CollectionUtil.isNullOrEmpty(content.getHierarchyInfo())) {
            intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_CONTENT_EXTRAS, GsonUtil.toJson(content.getHierarchyInfo()));
        }
        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_APP_INFO, GsonUtil.toJson(contentData));
        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_LANGUAGE_INFO, GsonUtil.toJson(resourceBundle));
        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_APP_QUALIFIER, sContentPlayer.mQualifier);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        context.startActivity(intent);
    }

    private static boolean isEcmlOrHtml(String mimeType) {
        return ContentConstants.MimeType.ECML.equals(mimeType)
                || ContentConstants.MimeType.HTML.equals(mimeType);

    }

    private static boolean isApk(String mimeType) {
        return ContentConstants.MimeType.APK.equals(mimeType);
    }
}
