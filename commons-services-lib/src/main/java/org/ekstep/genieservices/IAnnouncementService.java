package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.Announcement;
import org.ekstep.genieservices.commons.bean.AnnouncementRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.UpdateAnnouncementStateRequest;
import org.ekstep.genieservices.commons.bean.AnnouncementList;
import org.ekstep.genieservices.commons.bean.AnnouncementListRequest;

/**
 * This is the interface with all the required APIs to perform necessary operations related to Announcement.
 *
 * @author indraja on 29/3/18.
 */

public interface IAnnouncementService {

    /**
     * This api gets the announcement by Id.
     * <p>
     * On successful, the response will return status as TRUE and with {@link Announcement} in the result.
     * <p>
     * <p>
     * On failing, the response will return status as FALSE with the following error code
     *
     * @param announcementRequest - {@link AnnouncementRequest}
     * @return {@link GenieResponse<Announcement>}
     */
    GenieResponse<Announcement> getAnnouncementDetails(AnnouncementRequest announcementRequest);

    /**
     * This api is used to get the user inbox
     * <p>
     * <p>
     * On successful, the response will return status as TRUE.
     * <p>
     * <p>
     * On failing, the response will return status as FALSE with the following error code
     *
     * @param announcementListRequest - {@link AnnouncementListRequest}
     * @return {@link GenieResponse< AnnouncementList >}
     */
    GenieResponse<AnnouncementList> getAnnouncementList(AnnouncementListRequest announcementListRequest);

    /**
     * This api is used for update announcement state as received/read
     * <p>
     * <p>
     * On successful, the response will return status as TRUE.
     * <p>
     * <p>
     * On failing, the response will return status as FALSE with the following error code
     *
     * @param updateAnnouncementStateRequest - {@link UpdateAnnouncementStateRequest}
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> updateAnnouncementState(UpdateAnnouncementStateRequest updateAnnouncementStateRequest);
}
