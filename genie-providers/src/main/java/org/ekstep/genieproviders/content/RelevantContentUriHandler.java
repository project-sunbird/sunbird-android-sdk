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
import org.ekstep.genieservices.commons.bean.CorrelationData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.HierarchyInfo;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 12/7/2018.
 *
 * @author anil
 */
public class RelevantContentUriHandler implements IUriHandler {

    private final String TAG = RelevantContentUriHandler.class.getSimpleName();

    private String selection;
    private String authority;
    private GenieService genieService;

    public RelevantContentUriHandler(String authority, Context context, String selection, String[] selectionArgs, GenieService genieService) {
        this.authority = authority;
        this.selection = selection;
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

            String currentContentIdentifier = data.get("contentIdentifier").toString();
            List<Map> cdataListMap = (List<Map>) data.get("hierarchyInfo");

            GenieResponse genieResponse = null;
            if (cdataListMap != null && !cdataListMap.isEmpty()
                    && !StringUtil.isNullOrEmpty(currentContentIdentifier)) {
                Map<String, Object> resultMap = new HashMap<>();

                Type cdataType = new TypeToken<List<CorrelationData>>() {
                }.getType();

                String cdataJson = GsonUtil.getGson().toJson(cdataListMap);
                List<CorrelationData> cdata = GsonUtil.getGson().fromJson(cdataJson, cdataType);

                // Next Content
                boolean next = false;
                if (data.containsKey("next")) {
                    next = (boolean) data.get("next");
                }
                if (next) {
                    Map<String, Object> nextMap = new HashMap<>();
                    Content nextContent = genieService.getContentService().nextContent(createHierarchyInfo(cdata), currentContentIdentifier).getResult();
                    nextMap.put("content", nextContent);
                    if (nextContent != null) {
                        nextMap.put("cdata", createcDataList(nextContent.getHierarchyInfo()));
                    }
                    resultMap.put("next", nextMap);
                }

                // Previous Content
                boolean prev = false;
                if (data.containsKey("prev")) {
                    prev = (boolean) data.get("prev");
                }
                if (prev) {
                    Map<String, Object> prevMap = new HashMap<>();
                    Content prevContent = genieService.getContentService().prevContent(createHierarchyInfo(cdata), currentContentIdentifier).getResult();
                    prevMap.put("content", prevContent);
                    if (prevContent != null) {
                        prevMap.put("cdata", createcDataList(prevContent.getHierarchyInfo()));
                    }
                    resultMap.put("prev", prevMap);
                }

                genieResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                genieResponse.setResult(resultMap);
            }

            if (genieResponse != null) {
                cursor.addRow(new String[]{GsonUtil.toJson(genieResponse)});
            } else {
                getErrorResponse(cursor);
            }
        }

        return cursor;
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
        String contentListUri = String.format(Locale.US, "content://%s/relevantContent", authority);

        return uri != null && contentListUri.equals(uri.toString());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    private List<HierarchyInfo> createHierarchyInfo(List<CorrelationData> cdata) {
        List<HierarchyInfo> hierarchyInfo = new ArrayList<>();
        for (CorrelationData c : cdata) {
            hierarchyInfo.add(new HierarchyInfo(c.getId(), c.getType()));
        }
        return hierarchyInfo;
    }


    private List<CorrelationData> createcDataList(List<HierarchyInfo> hierarchyInfo) {
        List<CorrelationData> correlationDataList = new ArrayList<>();
        if (hierarchyInfo != null) {
            for (HierarchyInfo infoItem : hierarchyInfo) {
                correlationDataList.add(new CorrelationData(infoItem.getIdentifier(), infoItem.getContentType()));
            }
        }

        return correlationDataList;
    }
}
