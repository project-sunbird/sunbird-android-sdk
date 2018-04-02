package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.Announcement;
import org.ekstep.genieservices.commons.bean.AnnouncementRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ReceivedAnnouncementRequest;
import org.ekstep.genieservices.commons.bean.UserInboxAnnouncements;
import org.ekstep.genieservices.commons.bean.UserInboxRequest;

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
    GenieResponse<Announcement> getAnnouncementById(AnnouncementRequest announcementRequest);

    /**
     * This api is used to get the user inbox
     * <p>
     * <p>
     * On successful, the response will return status as TRUE.
     * <p>
     * <p>
     * On failing, the response will return status as FALSE with the following error code
     *
     * @param userInboxRequest - {@link UserInboxRequest}
     * @return {@link GenieResponse<UserInboxAnnouncements>}
     */
    GenieResponse<UserInboxAnnouncements> userInbox(UserInboxRequest userInboxRequest);

    /**
     * This api is used for received announcement
     * <p>
     * <p>
     * On successful, the response will return status as TRUE.
     * <p>
     * <p>
     * On failing, the response will return status as FALSE with the following error code
     *
     * @param receivedAnnouncementRequest - {@link ReceivedAnnouncementRequest}
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> receivedAnnouncement(ReceivedAnnouncementRequest receivedAnnouncementRequest);

    /**
     * This api is used to read announcement
     * <p>
     * <p>
     * On successful, the response will return status as TRUE.
     * <p>
     * <p>
     * On failing, the response will return status as FALSE with the following error code
     *
     * @param receivedAnnouncementRequest - {@link ReceivedAnnouncementRequest}
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> readAnnouncement(ReceivedAnnouncementRequest receivedAnnouncementRequest);

}
