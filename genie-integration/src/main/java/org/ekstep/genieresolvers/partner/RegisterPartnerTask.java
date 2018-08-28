package org.ekstep.genieresolvers.partner;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Map;

/**
 * Created on 24/5/17.
 * shriharsh
 */

public class RegisterPartnerTask extends BaseTask {
    private String appQualifier;
    private PartnerData partnerData;

    public RegisterPartnerTask(Context context, String appQualifier, PartnerData partnerData) {
        super(context);
        this.appQualifier = appQualifier;
        this.partnerData = partnerData;
    }

    @Override
    protected String getLogTag() {
        return null;
    }

    @Override
    protected GenieResponse<Map> execute() {
        ContentValues partnerValues = new ContentValues();
        partnerValues.put(Constants.PARTNER_DATA, GsonUtil.toJson(partnerData));
        Uri response = contentResolver.insert(getUri(), partnerValues);
        if (response == null) {
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), RegisterPartnerTask.class.getSimpleName());
        }

        return getSuccessResponse(Constants.SUCCESSFUL);
    }

    @Override
    protected String getErrorMessage() {
        return "Not able to register partner";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.partner", appQualifier);
        return Uri.parse(authority);
    }
}
