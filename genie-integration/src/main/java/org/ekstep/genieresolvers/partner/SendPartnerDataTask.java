package org.ekstep.genieresolvers.partner;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 25/5/17.
 * shriharsh
 */

public class SendPartnerDataTask extends BaseTask {

    private String appQualifier;
    private PartnerData partnerData;

    public SendPartnerDataTask(Context context, String appQualifier, PartnerData partnerData) {
        super(context);
        this.appQualifier = appQualifier;
        this.partnerData = partnerData;
    }

    private Uri getUri() {
        String authority = String.format("content://%s.partner/sendPartnerData", appQualifier);
        return Uri.parse(authority);
    }

    @Override
    protected String getLogTag() {
        return null;
    }

    @Override
    protected GenieResponse execute() {
        Cursor cursor = contentResolver.query(getUri(), null, GsonUtil.toJson(partnerData), null, "");
        GenieResponse response = getResponseFromCursor(cursor);

        if (response != null && response.getStatus()) {
            return GenieResponseBuilder.getSuccessResponse(ServiceConstants.ProviderResolver.SUCCESSFUL);
        }

        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR, getErrorMessage(), "Unable to send data!");
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
        return "Error in sending the data!";
    }
}
