package org.ekstep.genieresolvers.language;

import android.content.Context;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Language;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO THIS TASK IS CURRENTLY HARDCODED AND HAS TO IMLPEMENTED USING CONTENT RESOLVER
 * shriharsh
 */

public class GetAllLanguagesTask extends BaseTask {
    String LANG_ENGLISH = "en";
    String LANG_HINDI = "hi";
    String LANG_KANNADA = "kn";
    String LANG_TELUGU = "te";
    String LANG_MARATHI = "mr";
    String ENGLISH = "English";
    String HINDI = "Hindi";
    String KANNADA = "Kannada";
    String TELUGU = "Telugu";
    String MARATHI = "Marathi";
    private String appQualifier;

    public GetAllLanguagesTask(Context context, String appQualifier) {
        super(context);
        this.appQualifier = appQualifier;
    }

    private GenieResponse<List<Language>> getLanguageSuccessResponse(String message, List<Language> languages) {
        GenieResponse<List<Language>> response = new GenieResponse<>();
        response.setStatus(true);
        response.setMessage(message);
        response.setResult(languages);
        return response;
    }

    @Override
    protected String getLogTag() {
        return GetAllLanguagesTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse<Map> execute() {
        List<Language> languages = new ArrayList<>();

        Language enLanguage = new Language(LANG_ENGLISH, ENGLISH);
        languages.add(enLanguage);

        Language hiLanguage = new Language(LANG_HINDI, HINDI);
        languages.add(hiLanguage);

        Language knLanguage = new Language(LANG_KANNADA, KANNADA);
        languages.add(knLanguage);

        Language teLanguage = new Language(LANG_TELUGU, TELUGU);
        languages.add(teLanguage);

        Language mrLanguage = new Language(LANG_MARATHI, MARATHI);
        languages.add(mrLanguage);

        GenieResponse<List<Language>> response = getLanguageSuccessResponse(Constants.SUCCESSFUL, languages);

        return readCursor(GsonUtil.toJson(response));
    }

    private GenieResponse<Map> readCursor(String serverData) {
        GenieResponse<Map> response = GsonUtil.fromJson(serverData, GenieResponse.class);
        return response;
    }

    @Override
    protected String getErrorMessage() {
        return "Unable to fetch all languages!";
    }
}
