package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IConfigService;
import org.ekstep.genieservices.IFrameworkService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Category;
import org.ekstep.genieservices.commons.bean.CategoryDetailsRequest;
import org.ekstep.genieservices.commons.bean.Channel;
import org.ekstep.genieservices.commons.bean.ChannelDetailsRequest;
import org.ekstep.genieservices.commons.bean.Framework;
import org.ekstep.genieservices.commons.bean.FrameworkDetailsRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;

import java.util.List;
import java.util.Map;

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

    /**
     * This api is used to get the platform specific data, specific to locale chosen.
     * <p>
     * <p>
     * <p>
     * On successful fetching the data, the response will return status as TRUE and with List<Channel>
     * <p>
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     * <p>NO_DATA_FOUND
     *
     * @param categoryDetailsRequest
     * @param responseHandler        - {@link IResponseHandler<List<Channel>>}
     */
    public void getAllCategory(final CategoryDetailsRequest categoryDetailsRequest, IResponseHandler<List<Category>> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<List<Category>>() {
            @Override
            public GenieResponse<List<Category>> perform() {
                return frameworkService.getAllCategory(categoryDetailsRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used to get the ordered related data about the platform and other platform parameters.
     * <p>
     * <p>
     * On successful fetching the data, the response will return status as TRUE and with result set in Map
     * <p>
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     * <p>NO_DATA_FOUND
     *
     * @param categoryDetailsRequest
     * @param responseHandler        - {@link IResponseHandler<Category>}
     */
    public void getOrdinals(final CategoryDetailsRequest categoryDetailsRequest, IResponseHandler<Category> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Category>() {
            @Override
            public GenieResponse<Category> perform() {
                return frameworkService.getCategoryDetails(categoryDetailsRequest);
            }
        }, responseHandler);
    }
}
