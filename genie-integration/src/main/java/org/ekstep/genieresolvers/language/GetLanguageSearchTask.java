package org.ekstep.genieresolvers.language;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Map;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class GetLanguageSearchTask extends BaseTask {
    private String searchRequest;
    private String appQualifier;

    public GetLanguageSearchTask(Context context, String appQualifier, String searchRequest) {
        super(context);
        this.appQualifier = appQualifier;
        this.searchRequest = searchRequest;
    }

    @Override
    protected String getLogTag() {
        return GetLanguageSearchTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse<Map> execute() {
        Cursor cursor = contentResolver.query(getUri(), null, searchRequest, null, "");
        if (cursor == null) {
            return getErrorResponse(Constants.PROCESSING_ERROR,
                    getErrorMessage(), GetLanguageSearchTask.class.getSimpleName());
        }
        return getResponse(cursor);
    }

    private GenieResponse<Map> getResponse(Cursor cursor) {
        GenieResponse<Map> mapData = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                mapData = readCursor(cursor);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return mapData;
    }

    private GenieResponse<Map> readCursor(Cursor cursor) {
        String serverData = cursor.getString(0);
        GenieResponse<Map> response = GsonUtil.fromJson(serverData, GenieResponse.class);
        return response;
    }

    @Override
    protected String getErrorMessage() {
        return "Couldn't get the language search data";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.languages/langsearch", appQualifier);
        return Uri.parse(authority);
    }
}
