package org.ekstep.genieresolvers.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Map;

/**
 * Created on 12/7/2018.
 *
 * @author anil
 */
public class GetRelevantContentTask extends BaseTask {

    private String appQualifier;
    private String requestData;

    public GetRelevantContentTask(Context context, String appQualifier, String requestData) {
        super(context);
        this.appQualifier = appQualifier;
        this.requestData = requestData;
    }

    @Override
    protected String getLogTag() {
        return GetRelevantContentTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse<Map> execute() {
        Cursor cursor = contentResolver.query(getUri(), null, requestData, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), GetRelevantContentTask.class.getSimpleName());
        }
        return getResponse(cursor);
    }

    @Override
    protected String getErrorMessage() {
        return "Relevant content is not found with the content-ids";
    }

    private GenieResponse<Map> getResponse(Cursor cursor) {
        GenieResponse<Map> mapData = null;
        if (cursor != null && cursor.moveToFirst()) {
            mapData = readCursor(cursor);
            cursor.close();
        }
        return mapData;
    }

    private GenieResponse<Map> readCursor(Cursor cursor) {
        String resultData = cursor.getString(0);
        GenieResponse response = GsonUtil.fromJson(resultData, GenieResponse.class);
        return response;
    }

    private Uri getUri() {
        String authority = String.format("content://%s.content/relevantContent", appQualifier);
        return Uri.parse(authority);
    }

}
