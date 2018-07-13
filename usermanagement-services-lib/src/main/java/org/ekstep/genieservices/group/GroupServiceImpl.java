package org.ekstep.genieservices.group;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IGroupService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Group;

import java.util.List;


/**
 * Any service related to Group will be called using GroupServiceImpl
 * <p>
 * Created by swayangjit on 13/7/18.
 */
public class GroupServiceImpl extends BaseService implements IGroupService {

    private static final String TAG = GroupServiceImpl.class.getSimpleName();

    public GroupServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Group> createGroup(Group group) {
        return null;
    }

    @Override
    public GenieResponse<List<Group>> createGroups(List<Group> groupList) {
        return null;
    }

    @Override
    public GenieResponse<List<Group>> getAllGroups() {
        return null;
    }

    @Override
    public GenieResponse<Void> deleteGroup(String gid) {
        return null;
    }

    @Override
    public GenieResponse<Void> setCurrentGroup(String gid) {
        return null;
    }

    @Override
    public GenieResponse<Group> getCurrentGroup() {
        return null;
    }

    @Override
    public GenieResponse<Group> updateGroup(Group group) {
        return null;
    }
}
