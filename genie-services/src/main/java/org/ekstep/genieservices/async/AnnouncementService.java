package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IAnnouncementService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Announcement;
import org.ekstep.genieservices.commons.bean.AnnouncementRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.UserInboxRequest;

/**
 * This class provides APIs for performing {@link AnnouncementService} related operations on a separate thread.
 */
public class AnnouncementService {

    private IAnnouncementService announcementService;

    public AnnouncementService(GenieService genieService) {
        this.announcementService = genieService.getAnnouncementService();
    }

    /**
     * This api is used to get the announcement by Id
     *
     * @param announcementRequest - {@link AnnouncementRequest}
     * @param responseHandler     - {@link IResponseHandler <EnrolledCoursesResponse>}
     */
    public void getAnnouncementById(final AnnouncementRequest announcementRequest, IResponseHandler<Announcement> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Announcement>() {
            @Override
            public GenieResponse<Announcement> perform() {
                return announcementService.getAnnouncementById(announcementRequest);
            }
        }, responseHandler);
    }


    /**
     * This api is used to get the user inbox
     *
     * @param userInboxRequest - {@link UserInboxRequest}
     * @param responseHandler  - {@link IResponseHandler <Void>}
     */
    public void userInbox(final UserInboxRequest userInboxRequest, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return announcementService.userInbox(userInboxRequest);
            }
        }, responseHandler);
    }

}
