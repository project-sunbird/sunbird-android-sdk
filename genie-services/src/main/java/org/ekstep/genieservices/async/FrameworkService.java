package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IFrameworkService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Channel;
import org.ekstep.genieservices.commons.bean.ChannelDetailsRequest;
import org.ekstep.genieservices.commons.bean.Framework;
import org.ekstep.genieservices.commons.bean.FrameworkDetailsRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;

/**
 * This class provides APIs for performing {@link FrameworkService} related operations on a separate thread.
 *
 * @author indraja
 */
public class FrameworkService {

    private IFrameworkService frameworkService;

    public FrameworkService(GenieService genieService) {
        this.frameworkService = genieService.getFrameworkService();
    }

    /**
     * This api is used to get all the platform specific data.
     * <p>
     * <p>
     * On successful fetching the data, the response will return status as TRUE and with List<Channel> in the result
     * <p>
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     * <p>NO_DATA_FOUND
     *
     * @param request         - {@link ChannelDetailsRequest}
     * @param responseHandler - {@link IResponseHandler<Channel>}
     */
    public void getChannelDetails(final ChannelDetailsRequest request, final IResponseHandler<Channel> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Channel>() {
            @Override
            public GenieResponse<Channel> perform() {
                return frameworkService.getChannelDetails(request);
            }
        }, responseHandler);
    }

    /**
     * This api is used to get the specific platform data, selected from any of the {@link MasterDataType}
     * <p>
     * <p>
     * On successful fetching the data, the response will return status as TRUE and with MasterData in the result
     * <p>
     * <p>
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     * <p>NO_DATA_FOUND
     *
     * @param frameworkDetailsRequest -{@link FrameworkDetailsRequest}
     * @param responseHandler         - {@link IResponseHandler<Framework>}
     */
    public void getFrameworkDetails(final FrameworkDetailsRequest frameworkDetailsRequest, IResponseHandler<Framework> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Framework>() {
            @Override
            public GenieResponse<Framework> perform() {
                return frameworkService.getFrameworkDetails(frameworkDetailsRequest);
            }
        }, responseHandler);
    }

}
