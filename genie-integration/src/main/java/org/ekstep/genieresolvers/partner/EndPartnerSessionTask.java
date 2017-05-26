package org.ekstep.genieresolvers.partner;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 25/5/17.
 * shriharsh
 */

public class EndPartnerSessionTask extends BaseTask {
    private String appQualifier;
    private PartnerData partnerData;

    public EndPartnerSessionTask(Context context, String appQualifier, PartnerData partnerData) {
        super(context);
        this.appQualifier = appQualifier;
        this.partnerData = partnerData;
    }

    @Override
    protected String getLogTag() {
        return EndPartnerSessionTask.class.getSimpleName();
    }

    private Uri getUri() {
        String authority = String.format("content://%s.partner/endPartnerSession", appQualifier);
        return Uri.parse(authority);
    }

    @Override
    protected GenieResponse execute() {
        Cursor cursor = contentResolver.query(getUri(), null, GsonUtil.toJson(partnerData), null, "");
        GenieResponse response = getResponseFromCursor(cursor);

        if (response != null && response.getStatus()) {
            return getSuccessResponse(Constants.SUCCESSFUL);
        }

        return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), "Unable to end the session!");
    }


    private GenieResponse getResponseFromCursor(Cursor cursor) {
        GenieResponse response = null;
        if (cursor != null && cursor.moveToFirst()) {
            String resultData = cursor.getString(0);
            response = GsonUtil.fromJson(resultData, GenieResponse.class);
            cursor.close();
        }

        return response;
    }


    @Override
    protected String getErrorMessage() {
        return "Error in ending the session!";
    }
}
