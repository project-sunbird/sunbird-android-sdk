package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Locale;
import java.util.Map;

public class FeedbackUriHandler implements IUriHandler {
    private String authority;
    private Context context;
    private GenieService genieService;

    public FeedbackUriHandler(String authority, Context context, String selection, String[] selectionArgs, GenieService genieService) {
        this(authority, context, genieService);
    }

    public FeedbackUriHandler(String authority, Context context, GenieService genieService) {
        this.authority = authority;
        this.context = context;
        this.genieService = genieService;
    }

    @Override
    public Cursor process() {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String feedBackString = values.getAsString("event");
        Map<String, String> feedbackMap = GsonUtil.fromJson(feedBackString, Map.class);
        ContentFeedback contentFeedback = new ContentFeedback();
        contentFeedback.setContentId(feedbackMap.get("contentId"));
        if (feedbackMap.get("rating") != null) {
            contentFeedback.setRating(Float.valueOf(feedbackMap.get("rating")));
        }
        contentFeedback.setComments(feedbackMap.get("comments"));

        GenieResponse response = genieService.getContentFeedbackService().sendFeedback(contentFeedback, feedbackMap.get("stageId"));

        if (response != null && response.getStatus()) {
            return uri;
        }
        return null;
    }

    @Override
    public boolean canProcess(Uri uri) {
        String contentFeedbackUri = String.format(Locale.US, "content://%s/feedback", authority);
        return uri != null && contentFeedbackUri.equals(uri.toString());
    }

}
