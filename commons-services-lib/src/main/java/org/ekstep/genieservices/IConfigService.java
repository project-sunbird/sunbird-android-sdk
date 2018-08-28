package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;

import java.util.List;
import java.util.Map;

/**
 * This is the interface with all the required APIs to get platform specific data
 */
public interface IConfigService {

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
     * @return {@link GenieResponse<List<MasterData>>}
     */
    GenieResponse<List<MasterData>> getAllMasterData();

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
     * @param type - {@link MasterDataType}
     * @return {@link GenieResponse<MasterData>}
     */
    GenieResponse<MasterData> getMasterData(MasterDataType type);

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
     * @param languageIdentifier - {@link String}
     * @return {@link GenieResponse<Map<String, Object>>}
     */
    GenieResponse<Map<String, Object>> getResourceBundle(String languageIdentifier);

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
     * @return {@link GenieResponse<Map<String, Object>>}
     */
    GenieResponse<Map<String, Object>> getOrdinals();
}
