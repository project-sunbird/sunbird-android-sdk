package org.ekstep.genieservices.config;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.config.db.model.Term;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public class ConfigService {

    //    private APILogger mApiLogger;
    private AppContext appContext;

    public ConfigService(AppContext appContext) {
        this.appContext = appContext;
    }

    /**
     * Get terms
     *
     * @param type
     * @param responseHandler
     */
    public void getMasterData(MasterDataType type, IResponseHandler<String> responseHandler) {
        GenieResponse<String> response = new GenieResponse<>();
        if (type != null) {
            response.setStatus(true);
            Term term = Term.find(appContext, type.getValue());
            response.setResult(term.getTermJson());
            responseHandler.onSuccess(response);
        } else {
            response.setStatus(false);
            responseHandler.onError(response);
        }
    }

}
