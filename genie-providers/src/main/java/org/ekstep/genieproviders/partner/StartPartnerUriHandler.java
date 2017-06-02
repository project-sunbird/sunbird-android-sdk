package org.ekstep.genieproviders.partner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieproviders.util.Constants;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;

import java.util.Locale;

/**
 * Created on 24/5/17.
 * shriharsh
 */

public class StartPartnerUriHandler implements IUriHandler {
    private final String TAG = StartPartnerUriHandler.class.getSimpleName();
    private String authority;
    private String selection;
    private GenieService genieService;

    public StartPartnerUriHandler(String authority, Context context, String selection, String[] selectionArgs, GenieService genieService) {
        this.authority = authority;
        this.selection = selection;
        this.genieService = genieService;
    }

    @Override
    public Cursor process() {
        if (selection != null) {
            Logger.i(TAG, "Partner Data - " + selection);
            PartnerData partnerData = GsonUtil.fromJson(selection, PartnerData.class);
            GenieResponse response = genieService.getPartnerService().startPartnerSession(partnerData);
            if (response != null) {
                return convertToCursor(response);
            }

            return null;
        }
        return null;
    }

    private Cursor convertToCursor(GenieResponse response) {
        String[] partnerColumns = {Constants.PARTNER_CURSOR_KEY};
        MatrixCursor cursor = new MatrixCursor(partnerColumns);
        return populate(cursor, response);
    }

    public Cursor populate(MatrixCursor cursor, GenieResponse response) {
        cursor.addRow(new Object[]{GsonUtil.toJson(response)});
        return cursor;
    }

    @Override
    public boolean canProcess(Uri uri) {
        String urlPath = String.format(Locale.US, "content://%s/startPartnerSession", authority);
        return uri != null && urlPath.equals(uri.toString());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
