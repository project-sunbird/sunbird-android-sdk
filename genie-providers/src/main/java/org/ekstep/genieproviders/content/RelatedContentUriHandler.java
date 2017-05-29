package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieproviders.util.Constants;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class RelatedContentUriHandler implements IUriHandler {

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
        if (genieService != null && selectionArgs[0] != null) {
            cursor = getMatrixCursor();
            Type type = new TypeToken<List<String>>() {
            }.getType();

            List<String> contentIdentifiers = GsonUtil.getGson().fromJson(selectionArgs[0], type);

            GenieResponse genieResponse = null;
            if (contentIdentifiers != null && contentIdentifiers.size() == 1) {
                genieResponse = genieService.getContentService().getRelatedContent(contentIdentifiers.get(0));
            } else if (contentIdentifiers != null && contentIdentifiers.size() > 1) {
                // TODO: 29/5/17 NEED TO DECIDE RESULT MAP KEY FOR NEXT CONTENT
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("nextContent", genieService.getContentService().nextContent(contentIdentifiers).getResult());
                genieResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                genieResponse.setResult(resultMap);
            }

            if (genieResponse != null) {
                cursor.addRow(new String[]{new Gson().toJson(genieResponse)});
            } else {
                getErrorResponse(cursor);
            }
        }

        return cursor;
    }

    @NonNull
    protected GenieResponse getErrorResponse(MatrixCursor cursor) {
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(Constants.PROCESSING_ERROR, "Could not find the content", "Failed");
        cursor.addRow(new String[]{new Gson().toJson(errorResponse)});
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
