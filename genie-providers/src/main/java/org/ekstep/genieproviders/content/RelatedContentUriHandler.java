package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieproviders.util.Constants;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.HierarchyInfo;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class RelatedContentUriHandler implements IUriHandler {
    private final String TAG = RelatedContentUriHandler.class.getSimpleName();
    private String selection;
    private String[] selectionArgs;
    private String authority;
    private Context context;
    private GenieService genieService;

    public RelatedContentUriHandler(String authority, Context context, String selection, String[] selectionArgs, GenieService genieService) {
        this.authority = authority;
        this.context = context;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.genieService = genieService;
    }

    @Override
    public Cursor process() {
        MatrixCursor cursor = null;
        if (genieService != null && selection != null) {
            cursor = getMatrixCursor();
            Logger.i(TAG, "Content Identifier - " + selection);
            Type type = new TypeToken<Map>() {
            }.getType();
            Map data = GsonUtil.getGson().fromJson(selection, type);

            Map hierarchyData = (Map) data.get("hierarchyData");

            String currentContentIdentifier = data.get("currentContentIdentifier").toString();
            String userId = data.get("userId").toString();

            GenieResponse genieResponse = null;
            Map<String, Object> resultMap = new HashMap<>();
            if (hierarchyData == null) {
                RelatedContentRequest request = new RelatedContentRequest.Builder().forContent(currentContentIdentifier).byUser(userId).build();
                genieResponse = genieService.getContentService().getRelatedContent(request);
                resultMap.put("nextContent", genieResponse.getResult());
            } else if (hierarchyData != null) {
                // TODO: 29/5/17 NEED TO DECIDE RESULT MAP KEY FOR NEXT CONTENT
                Content content = genieService.getContentService().nextContent(createHierarchyInfo(hierarchyData), currentContentIdentifier).getResult();
                resultMap.put("nextContent", content);
                if (content != null) {
                    resultMap.put("hierarchyData", createHierarchyData(content.getHierarchyInfo()));
                }
            }
            genieResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            genieResponse.setResult(resultMap);

            if (genieResponse != null) {
                cursor.addRow(new String[]{GsonUtil.toJson(genieResponse)});
            } else {
                getErrorResponse(cursor);
            }
        }

        return cursor;
    }

    private Map createHierarchyData(List<HierarchyInfo> hierarchyInfo) {
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

    private List<HierarchyInfo> createHierarchyInfo(Map hierarchyData) {
        List<HierarchyInfo> hierarchyInfo = new ArrayList<>();
        String[] identifiers = hierarchyData.get("id").toString().split("/");
        String identifierType = hierarchyData.get("type").toString();
        for (String id : identifiers) {
            hierarchyInfo.add(new HierarchyInfo(id, identifierType));
            //reset identifierType to null as we only get the root elements identifierType and we dont have to set idnetifier for the other elements
            identifierType = null;
        }
        return hierarchyInfo;
    }

    @NonNull
    protected GenieResponse getErrorResponse(MatrixCursor cursor) {
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(Constants.PROCESSING_ERROR, "Could not find the content", "Failed");
        cursor.addRow(new String[]{GsonUtil.toJson(errorResponse)});
        return errorResponse;
    }

    @NonNull
    protected MatrixCursor getMatrixCursor() {
        return new MatrixCursor(new String[]{"values"});
    }

    @Override
    public boolean canProcess(Uri uri) {
        String contentListUri = String.format(Locale.US, "content://%s/relatedContent", authority);

        return uri != null && contentListUri.equals(uri.toString());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
