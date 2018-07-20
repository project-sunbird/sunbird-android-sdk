package org.ekstep.genieresolvers.group;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

/**
 * Created on 20/7/18.
 * shriharsh
 */
public class GroupService extends BaseService {
    private String appQualifier;
    private Context context;

    public GroupService(String appQualifier, Context context) {
        this.appQualifier = appQualifier;
        this.context = context;
    }

    /**
     * This api sets the specific gid passed to it as active current group.
     * <p>
     * <p>
     * <p>On successful setting a group active, the response will return status as TRUE and with successful message
     * <p>
     * <p>On failing to set the uid to current user, the response will have status as FALSE with the following error:
     * <p>INVALID_GROUP
     *
     * @param gid - {@link String}
     * @return {@link String}
     */
    public void setCurrentGroup(String gid, IResponseHandler<Map> responseHandler) {
        SetCurrentGroupTask setCurrentGroupTask = new SetCurrentGroupTask(context, appQualifier, gid);
        createAndExecuteTask(responseHandler, setCurrentGroupTask);
    }


    /**
     * This api gets the current active group.
     * <p>
     * <p>On successful fetching the active group, the response will return status as TRUE and with the active group set in result.
     * <p>
     * <p>Their would be no failure case with this api.
     *
     * @return {@link GenieResponse < Group >}
     */
    public void getCurrentGroup(IResponseHandler<Map> responseHandler) {
        GetCurrentGroupTask getCurrentGroupTask = new GetCurrentGroupTask(context, appQualifier);
        createAndExecuteTask(responseHandler, getCurrentGroupTask);
    }

}
