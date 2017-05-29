package org.ekstep.genieproviders.partner;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.ekstep.genieproviders.BaseContentProvider;
import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.List;

/**
 * Created on 24/5/17.
 * shriharsh
 */

public abstract class AbstractPartnerProvider extends BaseContentProvider {

    private static final String PARTNER_DATA = "partner_data";

    @Override
    public boolean onCreate() {
        return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        List<IUriHandler> handlers = PartnerUriHandlerFactory.uriHandlers(getCompletePath(), getContext(), selection, selectionArgs, getService());
        for (IUriHandler handler : handlers) {
            if (handler.canProcess(uri)) {
                return handler.process();
            }
        }

        return null;
    }

    @Override
    public String getType(Uri uri) {
        return String.format("vnd.android.cursor.item/%s.provider.partner", getPackageName());
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        PartnerData partnerData = GsonUtil.fromJson(values.getAsString(PARTNER_DATA), PartnerData.class);
        GenieResponse response = getService().getPartnerService().registerPartner(partnerData);
        if (response.getStatus()) {
            return uri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private String getCompletePath() {
        String PARTNER_PATH = ".partner";
        return String.format("%s.%s", getPackageName(), PARTNER_PATH);
    }
}
