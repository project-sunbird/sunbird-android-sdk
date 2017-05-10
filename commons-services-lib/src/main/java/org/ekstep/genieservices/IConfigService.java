package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;

import java.util.List;
import java.util.Map;

/**
 * Created on 5/10/2017.
 *
 * @author anil
 */
public interface IConfigService {

    GenieResponse<List<MasterData>> getAllMasterData();

    /**
     * Get terms
     *
     * @param type
     */
    GenieResponse<MasterData> getMasterData(MasterDataType type);

    /**
     * Get resource bundles
     *
     * @param languageIdentifier
     */
    GenieResponse<Map<String, Object>> getResourceBundle(String languageIdentifier);

    GenieResponse<Map<String, Object>> getOrdinals();
}
