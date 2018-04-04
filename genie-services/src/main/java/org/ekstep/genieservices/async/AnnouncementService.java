package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IAnnouncementService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Announcement;
import org.ekstep.genieservices.commons.bean.AnnouncementDetailsRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.UpdateAnnouncementStateRequest;
import org.ekstep.genieservices.commons.bean.AnnouncementList;
import org.ekstep.genieservices.commons.bean.AnnouncementListRequest;

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
     * @param announcementRequest - {@link AnnouncementDetailsRequest}
     * @param responseHandler     - {@link IResponseHandler <EnrolledCoursesResponse>}
     */
    public void getAnnouncementDetails(final AnnouncementDetailsRequest announcementRequest, IResponseHandler<Announcement> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Announcement>() {
            @Override
            public GenieResponse<Announcement> perform() {
                return announcementService.getAnnouncementDetails(announcementRequest);
            }
        }, responseHandler);
    }


    /**
     * This api is used to get the user inbox
     *
     * @param userInboxRequest - {@link AnnouncementListRequest}
     * @param responseHandler  - {@link IResponseHandler <Void>}
     */
    public void getAnnouncementList(final AnnouncementListRequest userInboxRequest, IResponseHandler<AnnouncementList> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<AnnouncementList>() {
            @Override
            public GenieResponse<AnnouncementList> perform() {
                return announcementService.getAnnouncementList(userInboxRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used for received announcement
     *
     * @param updateAnnouncementStateRequest - {@link UpdateAnnouncementStateRequest}
     * @param {@link                         GenieResponse<Void>}
     */
    public void receivedAnnouncement(final UpdateAnnouncementStateRequest updateAnnouncementStateRequest, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return announcementService.updateAnnouncementState(updateAnnouncementStateRequest);
            }
        }, responseHandler);
    }
}
