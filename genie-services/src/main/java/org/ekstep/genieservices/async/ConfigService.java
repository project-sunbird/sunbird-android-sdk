package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IConfigService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;

import java.util.List;
import java.util.Map;

/**
 * This class provides APIs for performing {@link ConfigService} related operations on a separate thread.
 *
 */
public class ConfigService {

    private IConfigService configService;

    public ConfigService(GenieService genieService) {
        this.configService = genieService.getConfigService();
    }

    /**
     * This api is used to get all the platform specific data.
     * <p>
     * <p>
     * On successful fetching the data, the response will return status as TRUE and with List<MasterData> in the result
     * <p>
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     * <p>NO_DATA_FOUND
     *
     * @param responseHandler - {@link IResponseHandler<List<MasterData>>}
     */
    public void getAllMasterData(IResponseHandler<List<MasterData>> responseHandler) {
        new AsyncHandler<List<MasterData>>(responseHandler).execute(new IPerformable<List<MasterData>>() {
            @Override
            public GenieResponse<List<MasterData>> perform() {
                return configService.getAllMasterData();
            }
        });
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
     * @param type -{@link MasterDataType}
     * @param responseHandler - {@link IResponseHandler<MasterData>}
     */
    public void getMasterData(final MasterDataType type, IResponseHandler<MasterData> responseHandler) {
        new AsyncHandler<MasterData>(responseHandler).execute(new IPerformable<MasterData>() {
            @Override
            public GenieResponse<MasterData> perform() {
                return configService.getMasterData(type);
            }
        });
    }

    /**
     * This api is used to get the platform specific data, specific to locale chosen.
     * <p>
     * <p>
     * <p>
     * On successful fetching the data, the response will return status as TRUE and with result set in Map
     * <p>
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     * <p>NO_DATA_FOUND
     *
     * @param languageIdentifier
     * @param responseHandler - {@link IResponseHandler<Map<String, Object>>}
     */
    public void getResourceBundle(final String languageIdentifier, IResponseHandler<Map<String, Object>> responseHandler) {
        new AsyncHandler<Map<String, Object>>(responseHandler).execute(new IPerformable<Map<String, Object>>() {
            @Override
            public GenieResponse<Map<String, Object>> perform() {
                return configService.getResourceBundle(languageIdentifier);
            }
        });
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
     * @param responseHandler - {@link IResponseHandler<Map<String, Object>>}
     */
    public void getOrdinals(IResponseHandler<Map<String, Object>> responseHandler) {
        new AsyncHandler<Map<String, Object>>(responseHandler).execute(new IPerformable<Map<String, Object>>() {
            @Override
            public GenieResponse<Map<String, Object>> perform() {
                return configService.getOrdinals();
            }
        });
    }
}
