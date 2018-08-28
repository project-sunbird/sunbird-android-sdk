package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.AddUpdateProfilesRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Group;
import org.ekstep.genieservices.commons.bean.GroupRequest;
import org.ekstep.genieservices.commons.bean.GroupSession;

import java.util.List;

/**
 * This is the interface with all the required APIs to perform necessary operations related to Groups
 */
public interface IGroupService {

    /**
     * This api is used to create a new group with specific {@link Group}.
     * <p>
     * <p>On Successful creation of new group, the response will return status as TRUE and with successful message
     * <p>
     * <p>On failing to create a group, the response will have status as FALSE with the following error:
     * <p>FAILED - createGroup
     *
     * @param group - {@link Group}
     * @return {@link GenieResponse<Group>}
     */
    GenieResponse<Group> createGroup(Group group);

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
     * @param group - {@link Group}
     * @return {@link GenieResponse<Group>}
     */
    GenieResponse<Group> updateGroup(Group group);

    /**
     * This api returns the list of all groups.
     *
     * @return {@link GenieResponse<List<Group>>}
     */
    GenieResponse<List<Group>> getAllGroup(GroupRequest groupRequest);

    /**
     * This api is used to delete a existing group with a specific gid
     * <p>
     * <p>and with result set as Profile related data
     * <p>
     * <p>On failing to delete group, the response will have status as FALSE with the following error:
     * <p>FAILED
     *
     * @param gid - {@link String}
     * @return {@link String}
     */
    GenieResponse<Void> deleteGroup(String gid);

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
    GenieResponse<Void> setCurrentGroup(String gid);

    /**
     * This api gets the current active group.
     * <p>
     * <p>On successful fetching the active group, the response will return status as TRUE and with the active group set in result.
     * <p>
     * <p>Their would be no failure case with this api.
     *
     * @return {@link GenieResponse<Group>}
     */
    GenieResponse<Group> getCurrentGroup();

    /**
     * This api gets the current active group session.
     * <p>
     * <p>On successful fetching the active group session, the response will return status as TRUE and with the active group session set in result.
     *
     * @return {@link GenieResponse<GroupSession >}
     */
    GenieResponse<GroupSession> getCurrentGroupSession();


    /**
     * This api adds/updates all the profiles to the group.
     *
     * @param addUpdateProfilesRequest
     * @return
     */
    GenieResponse<Void> addUpdateProfilesToGroup(AddUpdateProfilesRequest addUpdateProfilesRequest);

}
