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
 * Created on 22/5/17.
 */
public class ConfigService {

    private IConfigService configService;

    public ConfigService(GenieService genieService) {
        this.configService = genieService.getConfigService();
    }

    public void getAllMasterData(IResponseHandler<List<MasterData>> responseHandler) {
        new AsyncHandler<List<MasterData>>(responseHandler).execute(new IPerformable<List<MasterData>>() {
            @Override
            public GenieResponse<List<MasterData>> perform() {
                return configService.getAllMasterData();
            }
        });
    }

    public void getMasterData(final MasterDataType type, IResponseHandler<MasterData> responseHandler) {
        new AsyncHandler<MasterData>(responseHandler).execute(new IPerformable<MasterData>() {
            @Override
            public GenieResponse<MasterData> perform() {
                return configService.getMasterData(type);
            }
        });
    }

    public void getResourceBundle(final String languageIdentifier, IResponseHandler<Map<String, Object>> responseHandler) {
        new AsyncHandler<Map<String, Object>>(responseHandler).execute(new IPerformable<Map<String, Object>>() {
            @Override
            public GenieResponse<Map<String, Object>> perform() {
                return configService.getResourceBundle(languageIdentifier);
            }
        });
    }

    public void getOrdinals(IResponseHandler<Map<String, Object>> responseHandler) {
        new AsyncHandler<Map<String, Object>>(responseHandler).execute(new IPerformable<Map<String, Object>>() {
            @Override
            public GenieResponse<Map<String, Object>> perform() {
                return configService.getOrdinals();
            }
        });
    }
}
