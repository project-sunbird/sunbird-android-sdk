package org.ekstep.genieservices.configs;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.CallBack;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.configs.db.model.Term;
import org.ekstep.genieservices.configs.model.enums.MasterDataType;

import java.util.List;

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
     * @param callBack
     */
    public void getMasterData(MasterDataType type, CallBack<String> callBack) {
        GenieResponse<String> response = new GenieResponse<>();
        if (type != null) {
            response.setStatus(true);
            Term term = Term.find(appContext, type.getValue());
            response.setResult(term.getTermJson());
            callBack.onSuccess(response);
        } else {
            response.setStatus(false);
            callBack.onError(response);
        }
    }

}
