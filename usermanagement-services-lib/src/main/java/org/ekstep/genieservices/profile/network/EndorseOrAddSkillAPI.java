package org.ekstep.genieservices.profile.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 07/03/2018.
 *
 * @author anil
 */
public class EndorseOrAddSkillAPI extends BaseAPI {

    private static final String TAG = EndorseOrAddSkillAPI.class.getSimpleName();

    private static final String ENDPOINT = "skill/add";

    private String userId;
    private String[] skills;

    public EndorseOrAddSkillAPI(AppContext appContext, String userId, String[] skills) {
        super(appContext,
                String.format(Locale.US, "%s/%s", appContext.getParams().getString(IParams.Key.LANGUAGE_PLATFORM_BASE_URL),
                        ENDPOINT), TAG);

        this.userId = userId;
        this.skills = skills;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return null;
    }

    @Override
    protected String createRequestData() {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("endorsedUserId", userId);
        requestMap.put("skillName", skills);

        Map<String, Object> request = new HashMap<>();
        request.put("request", requestMap);
        return GsonUtil.toJson(request);
    }
}
