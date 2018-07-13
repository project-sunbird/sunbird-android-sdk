package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IGroupService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Group;

import java.util.List;

/**
 * Created by swayangjit on 13/7/18.
 */
public class GroupService {

    private IGroupService groupService;

    public GroupService(GenieService genieService) {
        this.groupService = genieService.getGroupService();
    }

    /**
     * This api is used to create a new group with specific {@link Group}.
     * <p>
     * <p>On Successful creation of new group, the response will return status as TRUE and with successful message
     * <p>
     * <p>On failing to create a group, the response will have status as FALSE with the following error:
     * <p>FAILED - createGroup
     *
     * @param group           - {@link Group}
     * @param responseHandler - {@link IResponseHandler<Group>}
     */
    public void createGroup(final Group group, IResponseHandler<Group> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Group>() {
            @Override
            public GenieResponse<Group> perform() {
                return groupService.createGroup(group);
            }
        }, responseHandler);
    }

    /**
     * This api is used to create  list of groups with specific {@link List<Group>}.
     * <p>
     * <p>On Successful creation of list group, the response will return status as TRUE and with successful message
     * <p>
     * <p>On failing to create a group, the response will have status as FALSE with the following error:
     * <p>FAILED - createGroup
     * <p>
     * * @param List<Group>         - {@link Group}
     *
     * @param responseHandler - {@link IResponseHandler<List<Group>>}
     */
    public void createGroups(final List<Group> groupList, IResponseHandler<List<Group>> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<List<Group>>() {
            @Override
            public GenieResponse<List<Group>> perform() {
                return groupService.createGroups(groupList);
            }
        }, responseHandler);
    }

    /**
     * This api returns the list of all groups.
     *
     * @param responseHandler {@link IResponseHandler<List<Group>>}
     */
    public void getAllGroups(IResponseHandler<List<Group>> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<List<Group>>() {
            @Override
            public GenieResponse<List<Group>> perform() {
                return groupService.getAllGroups();
            }
        }, responseHandler);
    }

    /**
     * This api is used to delete a existing group with a specific gid
     * <p>
     * <p>and with result set as Profile related data
     * <p>
     * <p>On failing to delete group, the response will have status as FALSE with the following error:
     * <p>FAILED
     *
     * @param gid
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void deleteGroup(final String gid, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return groupService.deleteGroup(gid);
            }
        }, responseHandler);
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
    public void setCurrentGroup(final String gid, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return groupService.setCurrentGroup(gid);
            }
        }, responseHandler);
    }


    /**
     * This api gets the current active group.
     * <p>
     * <p>On successful fetching the active group, the response will return status as TRUE and with the active group set in result.
     * <p>
     * <p>Their would be no failure case with this api.
     *
     * @param responseHandler - {@link IResponseHandler<Group>}
     */
    public void getCurrentGroup(IResponseHandler<Group> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Group>() {
            @Override
            public GenieResponse<Group> perform() {
                return groupService.getCurrentGroup();
            }
        }, responseHandler);
    }

    /**
     * This api updates the specific group.
     * <p>
     * <p>On successful updating the group, the response will return status as TRUE and with the updated group set in result.
     * <p>
     * <p>
     * <p>On failing to update the group, the response will have status as FALSE with one of the following errors:
     * <p>INVALID_GROUP
     * <p>VALIDATION_ERROR
     *
     * @param responseHandler - {@link IResponseHandler<Group>}
     */
    public void getCurrentUser(IResponseHandler<Group> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Group>() {
            @Override
            public GenieResponse<Group> perform() {
                return groupService.getCurrentGroup();
            }
        }, responseHandler);
    }

}
