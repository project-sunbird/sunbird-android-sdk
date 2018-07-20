package org.ekstep.genieresolvers.group;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Map;

/**
 * Created on 20/7/18.
 * shriharsh
 */
public class SetCurrentGroupTask extends BaseTask {

    private final String TAG = SetCurrentGroupTask.class.getSimpleName();
    private String appQualifier;
    private String gid;

    public SetCurrentGroupTask(Context context, String appQualifier, String gid) {
        super(context);
        this.appQualifier = appQualifier;
        this.gid = gid;
    }

    @Override
    protected String getLogTag() {
        return SetCurrentGroupTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse<Map> execute() {
        Cursor cursor = contentResolver.query(getUri(), null, gid, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), TAG);
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
        return "Could not set user!";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.groups/setGroup", appQualifier);
        return Uri.parse(authority);
    }
}
