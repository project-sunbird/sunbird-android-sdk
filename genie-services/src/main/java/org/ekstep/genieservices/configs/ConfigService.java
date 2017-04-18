package org.ekstep.genieservices.configs;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.CallBack;
import org.ekstep.genieservices.commons.Response;
import org.ekstep.genieservices.commons.db.operations.impl.SQLiteReader;
import org.ekstep.genieservices.commons.db.operations.impl.SQLiteSession;
import org.ekstep.genieservices.commons.utils.MapUtil;
import org.ekstep.genieservices.configs.db.model.Term;
import org.ekstep.genieservices.configs.model.enums.StaticDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public class ConfigService {

    //    private APILogger mApiLogger;
    private AppContext appContext;

    public ConfigService(AppContext appContext) {
//        this(new SQLiteSession(context), new APILogger(context, "ConfigService"), context);
    }

    /**
     * Get terms
     *
     * @param type
     * @param callBack
     */
    public void getStaticData(StaticDataType type, CallBack<List<String>> callBack) {
        Response<List<String>> response = new Response<>();
        if (type != null) {
            response.setStatus(true);
            List<String> list = new ArrayList<>();

            Term term = Term.find(appContext, type.getValue());
            Map<String, Object> result = new HashMap<>();
            Map termMap = MapUtil.toMap(term.getTermJson());
            List<LinkedTreeMap> termList = (List<LinkedTreeMap>) termMap.get("values");

            for (Map t : termList) {
                list.add(t.get("label").toString());
            }

            response.setInstance(list);
            callBack.onSuccess(response);
        } else {
            response.setStatus(false);
            callBack.onError(response);
        }
    }

}
