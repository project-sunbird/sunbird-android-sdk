package org.ekstep.genieservices.content;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.ICourseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.CourseBatchesRequest;
import org.ekstep.genieservices.commons.bean.CourseBatchesResponse;
import org.ekstep.genieservices.commons.bean.EnrolCourseRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.UpdateContentStateRequest;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class CourseServiceImpl extends BaseService implements ICourseService {

    private static final String TAG = CourseServiceImpl.class.getSimpleName();

    private static final String GET_ENROLLED_COURSES_KEY_PREFIX = "enrolledCourses";

    private IAuthSession<Session> authSession;

    public CourseServiceImpl(AppContext appContext, IAuthSession<Session> authSession) {
        super(appContext);
        this.authSession = authSession;
    }

    private <T> GenieResponse<T> isValidAuthSession(String methodName, Map<String, Object> params) {
        if (authSession == null || authSession.getSessionData() == null) {
            GenieResponse<T> response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.AUTH_SESSION,
                    ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN, TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN);
            return response;
        }

        return null;
    }

    @Override
    public GenieResponse<EnrolledCoursesResponse> getEnrolledCourses(EnrolledCoursesRequest enrolledCoursesRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(enrolledCoursesRequest));
        String methodName = "getEnrolledCourses@CourseServiceImpl";

        GenieResponse<EnrolledCoursesResponse> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        String key = GET_ENROLLED_COURSES_KEY_PREFIX + enrolledCoursesRequest.getUserId();
        NoSqlModel enrolledCoursesInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (enrolledCoursesInDB == null) {
            GenieResponse enrolledCoursesAPIResponse = CourseHandler.fetchEnrolledCoursesFromServer(mAppContext,
                    authSession.getSessionData(), enrolledCoursesRequest.getUserId());

            if (enrolledCoursesAPIResponse.getStatus()) {
                String body = enrolledCoursesAPIResponse.getResult().toString();
                enrolledCoursesInDB = NoSqlModel.build(mAppContext.getDBSession(), key, body);
                enrolledCoursesInDB.save();
            } else {
                response = GenieResponseBuilder.getErrorResponse(enrolledCoursesAPIResponse.getError(),
                        enrolledCoursesAPIResponse.getMessage(), TAG);

                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, enrolledCoursesAPIResponse.getMessage());
                return response;
            }
        } else if (enrolledCoursesRequest.isRefreshEnrolledCourses()) {
            CourseHandler.refreshEnrolledCoursesFromServer(mAppContext, authSession.getSessionData(),
                    enrolledCoursesRequest.getUserId(), enrolledCoursesInDB);
        }

        LinkedTreeMap map = GsonUtil.fromJson(enrolledCoursesInDB.getValue(), LinkedTreeMap.class);
        String result = GsonUtil.toJson(map.get("result"));
        EnrolledCoursesResponse enrolledCourses = GsonUtil.fromJson(result, EnrolledCoursesResponse.class);
        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(enrolledCourses);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<Void> enrolCourse(EnrolCourseRequest enrolCourseRequest) {
        return null;
    }

    @Override
    public GenieResponse<Void> updateContentState(UpdateContentStateRequest updateContentStateRequest) {
        return null;
    }

    @Override
    public GenieResponse<CourseBatchesResponse> getCourseBatches(CourseBatchesRequest courseBatchesRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(courseBatchesRequest));
        String methodName = "getCourseBatches@CourseServiceImpl";

        GenieResponse<CourseBatchesResponse> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse courseBatchesAPIResponse = CourseHandler.fetchCourseBatchesFromServer(mAppContext, authSession.getSessionData(), courseBatchesRequest);

        if (courseBatchesAPIResponse.getStatus()) {
            LinkedTreeMap map = GsonUtil.fromJson(courseBatchesAPIResponse.getResult().toString(), LinkedTreeMap.class);
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");
            String responseStr = GsonUtil.toJson(result.get("response"));

            CourseBatchesResponse searchUserResult = GsonUtil.fromJson(responseStr, CourseBatchesResponse.class);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(searchUserResult);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(courseBatchesAPIResponse.getError(), courseBatchesAPIResponse.getMessage(), TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, courseBatchesAPIResponse.getMessage());
        }

        return response;
    }
}
