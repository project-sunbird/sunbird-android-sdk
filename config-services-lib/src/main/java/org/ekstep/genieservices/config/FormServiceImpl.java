package org.ekstep.genieservices.config;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IFormService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.FormRequest;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.config.network.FormReadAPI;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 29/5/18.
 */
public class FormServiceImpl extends BaseService implements IFormService {

    private static final String TAG = FormServiceImpl.class.getSimpleName();
    private static final String SYLLABUS_INFO_KEY_PREFIX = "syllabusInfo-";
    private static final Double DEFAULT_TTL = 1d;   // In hours

    public FormServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Map<String, Object>> getForm(FormRequest formRequest) {
        String methodName = "getForm@ConfigServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        String key = getKeyForDB(formRequest);
        GenieResponse<Map<String, Object>> response;
        NoSqlModel syllabusInfoInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (syllabusInfoInDB == null) {
            FormReadAPI formReadAPI = new FormReadAPI(mAppContext, formRequest);
            GenieResponse formReadAPIResponse = formReadAPI.post();
            if (formReadAPIResponse.getStatus()) {
                String body = formReadAPIResponse.getResult().toString();
                syllabusInfoInDB = NoSqlModel.build(mAppContext.getDBSession(), key, body);
                syllabusInfoInDB.save();
            } else {
                List<String> errorMessages = formReadAPIResponse.getErrorMessages();
                String errorMessage = null;
                if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                    errorMessage = errorMessages.get(0);
                }
                response = GenieResponseBuilder.getErrorResponse(formReadAPIResponse.getError(), errorMessage, TAG);
                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
                return response;
            }
        }

        Map formData = GsonUtil.fromJson(syllabusInfoInDB.getValue(), Map.class);
        if (formData != null) {
            Map result = (Map) formData.get("result");
            Map form = (Map) result.get("form");

            if (form != null) {
                formData = (Map) form.get("data");
            }
        }

        if (formData != null) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(formData);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_FORM_DATA_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_FORM, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_FORM_DATA);
        }

        return response;
    }

    private String getKeyForDB(FormRequest formRequest) {
        return SYLLABUS_INFO_KEY_PREFIX + formRequest.toString();
    }

    private void saveDataExpirationTime(Double ttl, String key) {
        if (ttl == null || ttl == 0) {
            ttl = DEFAULT_TTL;
        }
        long ttlInMilliSeconds = (long) (ttl * DateUtil.MILLISECONDS_IN_AN_HOUR);
        Long currentTime = DateUtil.getEpochTime();
        long expiration_time = ttlInMilliSeconds + currentTime;

        mAppContext.getKeyValueStore().putLong(key, expiration_time);
    }

    private void persistSyllabusDetail() {

    }
}
