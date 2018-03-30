package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IAnnouncementService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Announcement;
import org.ekstep.genieservices.commons.bean.AnnouncementRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;

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
}
