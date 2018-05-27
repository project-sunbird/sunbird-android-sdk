package org.ekstep.genieservices.content;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.ICourseService;
import org.ekstep.genieservices.IUserProfileService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.Batch;
import org.ekstep.genieservices.commons.bean.BatchDetailsRequest;
import org.ekstep.genieservices.commons.bean.CourseBatchesRequest;
import org.ekstep.genieservices.commons.bean.CourseBatchesResponse;
import org.ekstep.genieservices.commons.bean.EnrollCourseRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.UpdateContentStateRequest;
import org.ekstep.genieservices.commons.bean.UserSearchCriteria;
import org.ekstep.genieservices.commons.bean.UserSearchResult;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class CourseServiceImpl extends BaseService implements ICourseService {

    private static final String TAG = CourseServiceImpl.class.getSimpleName();

    private static final String GET_ENROLLED_COURSES_KEY_PREFIX = "enrolledCourses";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_IDENTIFIER = "identifier";

    private IAuthSession<Session> authSession;
    private IUserProfileService userProfileService;

    public CourseServiceImpl(AppContext appContext, IAuthSession<Session> authSession, IUserProfileService userProfileService) {
        super(appContext);
        this.authSession = authSession;
        this.userProfileService = userProfileService;
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
        params.put("logLevel", "2");
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
                List<String> errorMessages = enrolledCoursesAPIResponse.getErrorMessages();
                String errorMessage = null;
                if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                    errorMessage = errorMessages.get(0);
                }
                response = GenieResponseBuilder.getErrorResponse(enrolledCoursesAPIResponse.getError(), errorMessage, TAG);
                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
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
    public GenieResponse<Void> enrollCourse(EnrollCourseRequest enrollCourseRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(enrollCourseRequest));
        params.put("logLevel", "2");
        String methodName = "enrollCourse@CourseServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse enrolCourseAPIResponse = CourseHandler.enrolCourseInServer(mAppContext, authSession.getSessionData(),
                enrollCourseRequest);

        if (enrolCourseAPIResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            List<String> errorMessages = enrolCourseAPIResponse.getErrorMessages();
            String errorMessage = null;
            if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                errorMessage = errorMessages.get(0);
            }
            response = GenieResponseBuilder.getErrorResponse(enrolCourseAPIResponse.getError(), errorMessage, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
        }

        return response;
    }

    @Override
    public GenieResponse<Void> updateContentState(UpdateContentStateRequest updateContentStateRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(updateContentStateRequest));
        params.put("logLevel", "2");
        String methodName = "updateContentState@CourseServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse updateContentStateAPIResponse = CourseHandler.updateContentStateInServer(mAppContext,
                authSession.getSessionData(), updateContentStateRequest);

        if (updateContentStateAPIResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            List<String> errorMessages = updateContentStateAPIResponse.getErrorMessages();
            String errorMessage = null;
            if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                errorMessage = errorMessages.get(0);
            }
            response = GenieResponseBuilder.getErrorResponse(updateContentStateAPIResponse.getError(), errorMessage, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
        }

        return response;
    }

    @Override
    public GenieResponse<CourseBatchesResponse> getCourseBatches(CourseBatchesRequest courseBatchesRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(courseBatchesRequest));
        params.put("logLevel", "2");
        String methodName = "getCourseBatches@CourseServiceImpl";

        GenieResponse<CourseBatchesResponse> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse courseBatchesAPIResponse = CourseHandler.fetchCourseBatchesFromServer(mAppContext,
                authSession.getSessionData(), courseBatchesRequest);

        if (courseBatchesAPIResponse.getStatus()) {
            LinkedTreeMap map = GsonUtil.fromJson(courseBatchesAPIResponse.getResult().toString(), LinkedTreeMap.class);
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");
            String responseStr = GsonUtil.toJson(result.get("response"));


            CourseBatchesResponse courseBatchesResponse = GsonUtil.fromJson(responseStr, CourseBatchesResponse.class);
            if (courseBatchesResponse.getCount() > 0) {
                List<Batch> batchList = courseBatchesResponse.getBatches();

                Set<String> identifiers = new LinkedHashSet<>();
                for (Batch batch : batchList) {
                    identifiers.add(batch.getCreatedBy());
                }
                UserSearchCriteria.SearchBuilder builder = new UserSearchCriteria.SearchBuilder().identifiers(identifiers).
                        limit(batchList.size()).fields(Arrays.asList(KEY_FIRST_NAME, KEY_LAST_NAME, KEY_IDENTIFIER));

                GenieResponse<UserSearchResult> searchUserResponse = userProfileService.searchUser(builder.build());
                if (searchUserResponse.getStatus()) {
                    Map resultMap = GsonUtil.fromJson(searchUserResponse.getResult().getSearchUserResult(), Map.class);

                    List<Map<String, String>> responseMapList = ((List<Map<String, String>>) resultMap.get("content"));
                    if (responseMapList != null && responseMapList.size() > 0) {
                        for (int i = 0; i < batchList.size(); i++) {
                            Batch batch = batchList.get(i);
                            for (Map<String, String> eachMap : responseMapList) {
                                if (batch.getCreatedBy().equalsIgnoreCase(eachMap.get("identifier").toString())) {

                                    if (eachMap.get("firstName") != null) {
                                        batchList.get(i).setCreatorFirstName(eachMap.get("firstName"));
                                    }

                                    if (eachMap.get("lastName") != null) {
                                        batchList.get(i).setCreatorLastName(eachMap.get("lastName"));
                                    }
                                }
                            }
                        }

                    }
                }
            }

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(courseBatchesResponse);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            List<String> errorMessages = courseBatchesAPIResponse.getErrorMessages();
            String errorMessage = null;
            if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                errorMessage = errorMessages.get(0);
            }
            response = GenieResponseBuilder.getErrorResponse(courseBatchesAPIResponse.getError(), errorMessage, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
        }

        return response;
    }

    @Override
    public GenieResponse<Batch> getBatchDetails(BatchDetailsRequest batchDetailsRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(batchDetailsRequest));
        params.put("logLevel", "2");
        String methodName = "getBatchDetails@CourseServiceImpl";

        GenieResponse<Batch> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse batchDetailsAPIResponse = CourseHandler.fetchBatchDetailsFromServer(mAppContext,
                authSession.getSessionData(), batchDetailsRequest.getBatchId());

        if (batchDetailsAPIResponse.getStatus()) {
            LinkedTreeMap map = GsonUtil.fromJson(batchDetailsAPIResponse.getResult().toString(), LinkedTreeMap.class);
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");
            String responseStr = GsonUtil.toJson(result.get("response"));

            Batch batch = GsonUtil.fromJson(responseStr, Batch.class);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(batch);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            List<String> errorMessages = batchDetailsAPIResponse.getErrorMessages();
            String errorMessage = null;
            if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                errorMessage = errorMessages.get(0);
            }
            response = GenieResponseBuilder.getErrorResponse(batchDetailsAPIResponse.getError(), errorMessage, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
        }

        return response;
    }
}
