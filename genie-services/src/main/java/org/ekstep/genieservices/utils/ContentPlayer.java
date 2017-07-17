package org.ekstep.genieservices.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.IPlayerConfig;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentData;
import org.ekstep.genieservices.commons.bean.HierarchyInfo;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.ReflectionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 1/6/17.
 *
 * @author swayangjit
 */
public class ContentPlayer {

    private static final String TAG = ContentPlayer.class.getSimpleName();

    private static ContentPlayer sContentPlayer;
    private String mQualifier;
    private IPlayerConfig playerConfig;

    private ContentPlayer(String qualifier, IPlayerConfig playerConfig) {
        this.mQualifier = qualifier;
        this.playerConfig = playerConfig;
    }

    public static void init(String qualifier, String playerConfigClass) {
        if (sContentPlayer == null) {
            IPlayerConfig playerConfig = null;
            if (playerConfigClass != null) {
                Class<?> classInstance = ReflectionUtil.getClass(playerConfigClass);
                if (classInstance != null) {
                    playerConfig = (IPlayerConfig) ReflectionUtil.getInstance(classInstance);
                }
            }

            sContentPlayer = new ContentPlayer(qualifier, playerConfig);
        }
    }

    public static void play(Context context, Content content, Map<String, Object> resourceBundle) {
        ContentData contentData = content.getContentData();
        if (sContentPlayer.mQualifier == null) {
            Toast.makeText(context, "App qualifier not found", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sContentPlayer.playerConfig == null) {
            Toast.makeText(context, "Implement IPlayerConfig and define in build.config", Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "Implement IPlayerConfig and define in build.config");
            return;
        }

        Intent intent = sContentPlayer.playerConfig.getPlayerIntent(context, content);
        if (intent == null) {
            return;
        }

        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_ORIGIN, "Genie");
        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_MODE, "play");
        if (!CollectionUtil.isNullOrEmpty(content.getHierarchyInfo())) {
            intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_CONTENT_EXTRAS, GsonUtil.toJson(createHierarchyData(content.getHierarchyInfo())));
        }
        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_APP_INFO, GsonUtil.toJson(contentData));
        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_LANGUAGE_INFO, GsonUtil.toJson(resourceBundle));
        intent.putExtra(ServiceConstants.BundleKey.BUNDLE_KEY_APP_QUALIFIER, sContentPlayer.mQualifier);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        context.startActivity(intent);
    }

    private static Map createHierarchyData(List<HierarchyInfo> hierarchyInfo) {
        Map hierarchyData = new HashMap();
        String id = "";
        String identifierType = null;
        for (HierarchyInfo infoItem : hierarchyInfo) {
            if (identifierType == null) {
                identifierType = infoItem.getContentType();
            }
            id += id.length() == 0 ? "" : "/";
            id += infoItem.getIdentifier();
        }
        hierarchyData.put("id", id);
        hierarchyData.put("type", identifierType);
        return hierarchyData;
    }
}
