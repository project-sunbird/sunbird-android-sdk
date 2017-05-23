package org.ekstep.genieresolvers;

import android.content.Context;

import org.ekstep.genieresolvers.content.GetContentTask;
import org.ekstep.genieresolvers.content.GetContentsTask;
import org.ekstep.genieresolvers.content.GetRelatedContentTask;
import org.ekstep.genieresolvers.content.SendFeedbackEventTask;
import org.ekstep.genieresolvers.language.GetAllLanguagesTask;
import org.ekstep.genieresolvers.language.GetLanguageSearchTask;
import org.ekstep.genieresolvers.language.GetTraversalRuleTask;
import org.ekstep.genieresolvers.telemetry.TelemetryEventTask;
import org.ekstep.genieresolvers.user.CreateUserTask;
import org.ekstep.genieresolvers.user.DeleteUserTask;
import org.ekstep.genieresolvers.user.GetCurrentUserTask;
import org.ekstep.genieresolvers.user.UpdateUserTask;
import org.ekstep.genieservices.commons.bean.Profile;

import java.util.HashMap;
import java.util.List;

/**
 * Created on 18/5/17.
 * shriharsh
 */

public class GenieResolver {

    private static GenieResolver sGenieResolver;
    private String appQualifier;
    private Context context;
    private GetContentTask mGetContentTask;
    private GetContentsTask mGetContentsTask;
    private GetTraversalRuleTask mGetTraversalRuleTask;
    private GetAllLanguagesTask mGetAllLanguagesTask;
    private GetLanguageSearchTask mGetLanguageSearchTask;
    private TelemetryEventTask mTelemetryEventTask;
    private CreateUserTask mCreateUserTask;
    private DeleteUserTask mDeleteUserTask;
    private UpdateUserTask mUpdateUserTask;
    private GetCurrentUserTask mGetCurrentUserTask;
    private GetRelatedContentTask mGetRelatedContentTask;
    private SendFeedbackEventTask mSendFeedbackEventTask;

    private GenieResolver(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    public static GenieResolver getGenieResolver() {
        return sGenieResolver;
    }

    public static GenieResolver init(Context context, String authorityName) {

        if (sGenieResolver == null) {
            sGenieResolver = new GenieResolver(context, authorityName);
        }
        return sGenieResolver;
    }

    public GetContentTask getContentTask(String contentId) {
        if (mGetContentTask == null) {
            mGetContentTask = new GetContentTask(context, appQualifier, contentId);
        }
        return mGetContentTask;
    }

    public GetContentsTask getContentsTask() {
        if (mGetContentsTask == null) {
            mGetContentsTask = new GetContentsTask(context, appQualifier);
        }
        return mGetContentsTask;
    }

    public GetTraversalRuleTask getTraversalRuleTask(String languageId) {
        if (mGetTraversalRuleTask == null) {
            mGetTraversalRuleTask = new GetTraversalRuleTask(context, appQualifier, languageId);
        }
        return mGetTraversalRuleTask;
    }

    public GetAllLanguagesTask getAllLanguagesTask() {
        if (mGetAllLanguagesTask == null) {
            mGetAllLanguagesTask = new GetAllLanguagesTask(context, appQualifier);
        }
        return mGetAllLanguagesTask;
    }

    public GetLanguageSearchTask getLanguageSearchTask(String searchRequest) {
        if (mGetLanguageSearchTask == null) {
            mGetLanguageSearchTask = new GetLanguageSearchTask(context, appQualifier, searchRequest);
        }
        return mGetLanguageSearchTask;
    }

    public TelemetryEventTask getmTelemetryEventTask(String eventString) {
        if (mTelemetryEventTask == null) {
            mTelemetryEventTask = new TelemetryEventTask(context, appQualifier, eventString);
        }
        return mTelemetryEventTask;
    }

    public CreateUserTask getCreateUserTask(Profile profile) {
        if (mCreateUserTask == null) {
            mCreateUserTask = new CreateUserTask(context, appQualifier, profile);
        }
        return mCreateUserTask;
    }

    public DeleteUserTask getDeleteUserTask(String userId) {
        if (mDeleteUserTask == null) {
            mDeleteUserTask = new DeleteUserTask(context, appQualifier, userId);
        }
        return mDeleteUserTask;
    }

    public UpdateUserTask getUpdateUserTask(Profile profile) {
        if (mUpdateUserTask == null) {
            mUpdateUserTask = new UpdateUserTask(context, appQualifier, profile);
        }
        return mUpdateUserTask;
    }

    public GetCurrentUserTask getCurrentUserTask() {
        if (mGetCurrentUserTask == null) {
            mGetCurrentUserTask = new GetCurrentUserTask(context, appQualifier);
        }
        return mGetCurrentUserTask;
    }

    public GetRelatedContentTask getRelatedContentTask(List<HashMap<String, Object>> contentIdentifiers, String userId) {
        if (mGetRelatedContentTask == null) {
            mGetRelatedContentTask = new GetRelatedContentTask(context, appQualifier, contentIdentifiers, userId);
        }
        return mGetRelatedContentTask;
    }

    public SendFeedbackEventTask getSendFeedbackEventTask(String feedbackString) {
        if (mSendFeedbackEventTask == null) {
            mSendFeedbackEventTask = new SendFeedbackEventTask(context, appQualifier, feedbackString);
        }
        return mSendFeedbackEventTask;
    }

}
