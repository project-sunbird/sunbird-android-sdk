package org.ekstep.genieservices.content;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.IContentService;
import org.ekstep.genieservices.ICourseService;
import org.ekstep.genieservices.IUserProfileService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.Batch;
import org.ekstep.genieservices.commons.bean.BatchDetailsRequest;
import org.ekstep.genieservices.commons.bean.ChildContentRequest;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentState;
import org.ekstep.genieservices.commons.bean.ContentStateResponse;
import org.ekstep.genieservices.commons.bean.Course;
import org.ekstep.genieservices.commons.bean.CourseBatchesRequest;
import org.ekstep.genieservices.commons.bean.CourseBatchesResponse;
import org.ekstep.genieservices.commons.bean.EnrollCourseRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.GetContentStateRequest;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.UnenrolCourseRequest;
import org.ekstep.genieservices.commons.bean.UpdateContentStateRequest;
import org.ekstep.genieservices.commons.bean.UserSearchCriteria;
import org.ekstep.genieservices.commons.bean.UserSearchResult;
import org.ekstep.genieservices.commons.bean.enums.CourseBatchStatus;
import org.ekstep.genieservices.commons.db.contract.NoSqlEntry;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.db.model.NoSqlModelListModel;
import org.ekstep.genieservices.commons.network.NetworkConstants;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
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
    private static final String GET_CONTENT_STATE_KEY_PREFIX = "getContentState";
    private static final String UPDATE_CONTENT_STATE_KEY_PREFIX = "updateContentState";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_IDENTIFIER = "identifier";

    private IAuthSession<Session> authSession;
    private IUserProfileService userProfileService;
    private IContentService contentService;
    private int leafNodeCount = 0;


    public CourseServiceImpl(AppContext appContext, IAuthSession<Session> authSession, IUserProfileService userProfileService, IContentService contentService) {
        super(appContext);
        this.authSession = authSession;
        this.userProfileService = userProfileService;
        this.contentService = contentService;
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

        //check if there are any content state to be updated
        if (mAppContext.getKeyValueStore().getBoolean(ServiceConstants.PreferenceKey.UPDATE_CONTENT_STATE, false)) {
            StringBuilder stringBuilder = new StringBuilder();

            String query = stringBuilder.append(String.format(Locale.US, "Select * from %s where %s like '%%%s%%'", NoSqlEntry.TABLE_NAME, NoSqlEntry.COLUMN_NAME_KEY, UPDATE_CONTENT_STATE_KEY_PREFIX)).toString();
            NoSqlModelListModel noSqlModelListModel = NoSqlModelListModel.findWithCustomQuery(mAppContext.getDBSession(), query);


            if (noSqlModelListModel != null) {
                HashMap<String, List<UpdateContentStateRequest>> userContentStateMap = new HashMap<>();

                for (NoSqlModel noSqlModel : noSqlModelListModel.getNoSqlModelList()) {
                    UpdateContentStateRequest updateContentStateRequest = GsonUtil.fromJson(noSqlModel.getValue(), UpdateContentStateRequest.class);

                    if (updateContentStateRequest != null && updateContentStateRequest.getUserId() != null)
                        if (userContentStateMap.containsKey(updateContentStateRequest.getUserId())) {
                            userContentStateMap.get(updateContentStateRequest.getUserId()).add(updateContentStateRequest);
                        } else {
                            List<UpdateContentStateRequest> updateContentStateRequestList = new ArrayList<>();
                            updateContentStateRequestList.add(updateContentStateRequest);
                            userContentStateMap.put(updateContentStateRequest.getUserId(), updateContentStateRequestList);
                        }
                }

                //update the content state to server
                updateContentState(userContentStateMap);
            }
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
        } else if (enrolledCoursesRequest.isReturnRefreshedEnrolledCourses()) {
            GenieResponse enrolledCoursesAPIResponse = CourseHandler.fetchEnrolledCoursesFromServer(mAppContext,
                    authSession.getSessionData(), enrolledCoursesRequest.getUserId());

            if (enrolledCoursesAPIResponse.getStatus()) {
                String jsonResponse = enrolledCoursesAPIResponse.getResult().toString();
                if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                    enrolledCoursesInDB.setValue(jsonResponse);
                    enrolledCoursesInDB.update();
                }
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

    private GenieResponse<Void> updateContentState(Map<String, List<UpdateContentStateRequest>> userContentStateMap) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(userContentStateMap));
        params.put("logLevel", "2");
        String methodName = "updateContentState@CourseServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse updateContentStateAPIResponse = CourseHandler.updateContentStateListInServer(mAppContext,
                authSession.getSessionData(), userContentStateMap);

        if (updateContentStateAPIResponse.getStatus()) {

            //update all the content states first
            String filter = String.format(Locale.US, "where %s like '%%%s%%'", NoSqlEntry.COLUMN_NAME_KEY, UPDATE_CONTENT_STATE_KEY_PREFIX);
            NoSqlModelListModel noSqlModelListModel = NoSqlModelListModel.findWithFilter(mAppContext.getDBSession(), filter);

            if (noSqlModelListModel != null) {
                noSqlModelListModel.delete();
            }

            //update the preference to false
            mAppContext.getKeyValueStore().putBoolean(ServiceConstants.PreferenceKey.UPDATE_CONTENT_STATE, false);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        }

        return response;
    }

    @Override
    public GenieResponse<Void> enrollCourse(EnrollCourseRequest enrollCourseRequest) {
        leafNodeCount = 0;
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

            //wipe the details of course context
            mAppContext.getKeyValueStore().putString(ServiceConstants.PreferenceKey.SUNBIRD_CONTENT_CONTEXT, "");

            //add the course details to map
            Map<String, String> courseContext = new HashMap<>();
            courseContext.put("userId", enrollCourseRequest.getUserId());
            courseContext.put("courseId", enrollCourseRequest.getCourseId());
            courseContext.put("batchId", enrollCourseRequest.getBatchId());

            //store the newly enrolled course in the preference
            mAppContext.getKeyValueStore().putString(ServiceConstants.PreferenceKey.SUNBIRD_CONTENT_CONTEXT, GsonUtil.toJson(courseContext));

            addNewlyEnrolledCourseToGetEnrolledCourses(enrollCourseRequest);

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

    private void addNewlyEnrolledCourseToGetEnrolledCourses(EnrollCourseRequest enrollCourseRequest) {
        //get all the enrolled courses
        EnrolledCoursesResponse enrolledCoursesResponse = null;
        GenieResponse genieResponse = null;
        String key = GET_ENROLLED_COURSES_KEY_PREFIX + enrollCourseRequest.getUserId();
        NoSqlModel enrolledCoursesInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (enrolledCoursesInDB == null) {
            Course newlyEnrolledCourse = getNewlyAddedCourse(enrollCourseRequest);

            List<Course> courses = new ArrayList<>();
            //add to the courses
            courses.add(newlyEnrolledCourse);

            //create dummy EnrolledCoursesResponse
            EnrolledCoursesResponse dummyEnrolledCoursesResponse = new EnrolledCoursesResponse();
            dummyEnrolledCoursesResponse.setCourses(courses);

            GenieResponse<EnrolledCoursesResponse> tempEnrolledCoursesResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, EnrolledCoursesResponse.class);
            tempEnrolledCoursesResponse.setResult(dummyEnrolledCoursesResponse);

            // Save to DB
            String body = tempEnrolledCoursesResponse.getResult().toString();
            enrolledCoursesInDB = NoSqlModel.build(mAppContext.getDBSession(), key, body);
            enrolledCoursesInDB.save();
        } else {
            genieResponse = GsonUtil.fromJson(enrolledCoursesInDB.getValue(), GenieResponse.class);
            LinkedTreeMap map = GsonUtil.fromJson(enrolledCoursesInDB.getValue(), LinkedTreeMap.class);
            String result = GsonUtil.toJson(map.get("result"));
            enrolledCoursesResponse = GsonUtil.fromJson(result, EnrolledCoursesResponse.class);
        }

        if (enrolledCoursesResponse != null && enrolledCoursesResponse.getCourses() != null && enrolledCoursesResponse.getCourses().size() > 0) {
            boolean isCoursePresent = false;
            //check if the enrolled courses already contains the course
            for (Course course : enrolledCoursesResponse.getCourses()) {
                if (course.getCourseId().equalsIgnoreCase(enrollCourseRequest.getCourseId())) {
                    isCoursePresent = true;
                }

            }

            if (!isCoursePresent) {
                Course newlyEnrolledCourse = getNewlyAddedCourse(enrollCourseRequest);

                List<Course> courseList = enrolledCoursesResponse.getCourses();

                //manipulate the enrolledCoursesList here and update the db
                courseList.add(newlyEnrolledCourse);

                //update this new course list to DB
                enrolledCoursesResponse.setCourses(courseList);

                //set it to the genie response
                genieResponse.setResult(enrolledCoursesResponse);

                //update the NoSQL db
                enrolledCoursesInDB.setValue(GsonUtil.toJson(genieResponse));
                enrolledCoursesInDB.update();
            }
        }
    }

    private Course getNewlyAddedCourse(EnrollCourseRequest enrollCourseRequest) {
        int leafCount = 0;
        ChildContentRequest.Builder childContentRequestBuilder = new ChildContentRequest.Builder().forContent(enrollCourseRequest.getCourseId());
        GenieResponse<Content> childContents = this.contentService.getChildContents(childContentRequestBuilder.build());

        if (childContents.getStatus() && childContents.getResult() != null)
            leafCount = getLeafNodeCount(childContents.getResult());


        //get the content details of the course
        ContentDetailsRequest.Builder contentDetailsBuilder = new ContentDetailsRequest.Builder().forContent(enrollCourseRequest.getCourseId());
        GenieResponse<Content> contentDetails = this.contentService.getContentDetails(contentDetailsBuilder.build());

        //create a new course
        Course newlyEnrolledCourse = new Course();
        newlyEnrolledCourse.setProgress(0);
        newlyEnrolledCourse.setUserId(enrollCourseRequest.getUserId());
        newlyEnrolledCourse.setBatchId(enrollCourseRequest.getBatchId());
        newlyEnrolledCourse.setCourseId(enrollCourseRequest.getCourseId());
        newlyEnrolledCourse.setContentId(enrollCourseRequest.getCourseId());
        newlyEnrolledCourse.setLeafNodesCount(leafCount);
        newlyEnrolledCourse.setActive(true);
        if (contentDetails != null && contentDetails.getResult() != null) {
            newlyEnrolledCourse.setCourseName(contentDetails.getResult().getContentData().getName());
            String baseUrl = contentDetails.getResult().getBasePath();
            String imageUrl = contentDetails.getResult().getContentData().getAppIcon();
            String logo = baseUrl + "/" + imageUrl;
            newlyEnrolledCourse.setCourseLogoUrl(logo);
        }
        return newlyEnrolledCourse;
    }

    @Override
    public GenieResponse<Void> unenrolCourse(UnenrolCourseRequest unenrolCourseRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(unenrolCourseRequest));
        params.put("logLevel", "2");
        String methodName = "unenrolCourse@CourseServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse unenrolCourseAPIResponse = CourseHandler.unenrolCourseInServer(mAppContext, authSession.getSessionData(),
                unenrolCourseRequest);

        if (unenrolCourseAPIResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            List<String> errorMessages = unenrolCourseAPIResponse.getErrorMessages();
            String errorMessage = null;
            if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                errorMessage = errorMessages.get(0);
            }
            response = GenieResponseBuilder.getErrorResponse(unenrolCourseAPIResponse.getError(), errorMessage, TAG);
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
            String error = updateContentStateAPIResponse.getError();
            String errorMessage = null;
            if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                errorMessage = errorMessages.get(0);

                //check if the errorMessage is of NETWORK_ERROR or CONNECTION_ERROR
                if (!StringUtil.isNullOrEmpty(error) && (error.equalsIgnoreCase(NetworkConstants.NETWORK_ERROR) || error.equalsIgnoreCase(NetworkConstants.CONNECTION_ERROR))) {
                    String key = UPDATE_CONTENT_STATE_KEY_PREFIX +
                            updateContentStateRequest.getUserId() +
                            updateContentStateRequest.getCourseId() +
                            updateContentStateRequest.getContentId() +
                            updateContentStateRequest.getBatchId();

                    NoSqlModel contentStateInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);

                    String body = GsonUtil.toJson(updateContentStateRequest);

                    if (contentStateInDB == null) {
                        contentStateInDB = NoSqlModel.build(mAppContext.getDBSession(), key, body);
                        contentStateInDB.save();
                    } else {
                        contentStateInDB.setValue(body);
                        contentStateInDB.update();
                    }

                    //store the preference in flag
                    mAppContext.getKeyValueStore().putBoolean(ServiceConstants.PreferenceKey.UPDATE_CONTENT_STATE, true);

                    //updating the course state for enrolled courses
                    String enrolledCoursesKey = GET_ENROLLED_COURSES_KEY_PREFIX + updateContentStateRequest.getUserId();
                    NoSqlModel enrolledCoursesInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), enrolledCoursesKey);

                    if (enrolledCoursesInDB != null) {
                        GenieResponse genieResponse = GsonUtil.fromJson(enrolledCoursesInDB.getValue(), GenieResponse.class);

                        LinkedTreeMap map = GsonUtil.fromJson(enrolledCoursesInDB.getValue(), LinkedTreeMap.class);
                        String result = GsonUtil.toJson(map.get("result"));
                        EnrolledCoursesResponse enrolledCoursesResponse = GsonUtil.fromJson(result, EnrolledCoursesResponse.class);

                        if (enrolledCoursesResponse != null && enrolledCoursesResponse.getCourses() != null && enrolledCoursesResponse.getCourses().size() > 0) {
                            List<Course> courseList = enrolledCoursesResponse.getCourses();
                            List<Course> newCourseList = new ArrayList<>(courseList);

                            for (Course course : courseList) {
                                if (course.getCourseId().equalsIgnoreCase(updateContentStateRequest.getCourseId())) {

                                    if (course.getContentsPlayedOffline().size() == 0 ||
                                            (course.getContentsPlayedOffline().size() > 0 && !course.getContentsPlayedOffline().contains(updateContentStateRequest.getContentId()))) {
                                        int newProgress = course.getProgress() + 1;

                                        Course updatedCourse = course;
                                        updatedCourse.setContentPlayedOffline(updateContentStateRequest.getContentId());
                                        updatedCourse.setProgress(newProgress);

                                        //remove old course
                                        newCourseList.remove(course);

                                        //add new course
                                        newCourseList.add(updatedCourse);
                                    }

                                }
                            }

                            if (newCourseList.size() > 0) {
                                //update the enrolled course response
                                enrolledCoursesResponse.setCourses(newCourseList);

                                //set it to the genie response
                                genieResponse.setResult(enrolledCoursesResponse);

                                //update the NoSQL db
                                enrolledCoursesInDB.setValue(GsonUtil.toJson(genieResponse));
                                enrolledCoursesInDB.update();
                            }
                        }
                    }
                }
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

        Map<String, Object> requestMap = getCourseBatchesRequest(courseBatchesRequest);


        GenieResponse<CourseBatchesResponse> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse courseBatchesAPIResponse = CourseHandler.fetchCourseBatchesFromServer(mAppContext,
                authSession.getSessionData(), requestMap);

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

    private Map<String, Object> getCourseBatchesRequest(CourseBatchesRequest courseBatchesRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("filters", getCourseBatchFilters(courseBatchesRequest));
        requestMap.put("sort_by", getBatchSortCriteria(courseBatchesRequest));
        return requestMap;
    }

    private Map<String, Object> getBatchSortCriteria(CourseBatchesRequest courseBatchesRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("createdDate", courseBatchesRequest.getSortBy());
        return requestMap;

    }

    private Map<String, Object> getCourseBatchFilters(CourseBatchesRequest courseBatchesRequest) {
        Map<String, Object> requestMap = new HashMap<>();

        CourseBatchStatus[] courseBatchStatuses = courseBatchesRequest.getStatus();

        if (courseBatchStatuses != null && courseBatchStatuses.length > 0) {
            String[] status = new String[courseBatchesRequest.getStatus().length];

            for (int i = 0; i < courseBatchesRequest.getStatus().length; i++) {
                status[i] = courseBatchStatuses[i].getValue();
            }

            requestMap.put("status", status);
        }
        requestMap.put("courseId", courseBatchesRequest.getCourseId());
        requestMap.put("enrollmentType", courseBatchesRequest.getEnrollmentType());
        return requestMap;
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

    @Override
    public GenieResponse<ContentStateResponse> getContentState(GetContentStateRequest contentStateRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(contentStateRequest));
        params.put("logLevel", "2");
        String methodName = "contentStateRequest@CourseServiceImpl";

        GenieResponse<ContentStateResponse> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }


        String key = GET_CONTENT_STATE_KEY_PREFIX + contentStateRequest.getUserId() + contentStateRequest.getCourseIds().get(0);

        NoSqlModel contentStateInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (contentStateInDB == null) {
            GenieResponse contentStateAPIResponse = CourseHandler.fetchContentStateFromServer(mAppContext,
                    authSession.getSessionData(), contentStateRequest);

            if (contentStateAPIResponse.getStatus()) {
                String body = contentStateAPIResponse.getResult().toString();
                contentStateInDB = NoSqlModel.build(mAppContext.getDBSession(), key, body);
                contentStateInDB.save();
            } else {
                List<String> errorMessages = contentStateAPIResponse.getErrorMessages();
                String errorMessage = null;
                if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                    errorMessage = errorMessages.get(0);
                }
                response = GenieResponseBuilder.getErrorResponse(contentStateAPIResponse.getError(), errorMessage, TAG);
                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
                return response;
            }
        } else if (contentStateRequest.isReturnRefreshedContentStates()) {
            GenieResponse contentStateAPIResponse = CourseHandler.fetchContentStateFromServer(mAppContext,
                    authSession.getSessionData(), contentStateRequest);

            if (contentStateAPIResponse.getStatus()) {
                String jsonResponse = contentStateAPIResponse.getResult().toString();
                if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                    contentStateInDB.setValue(jsonResponse);
                    contentStateInDB.update();
                }
            }
        }

        LinkedTreeMap map = GsonUtil.fromJson(contentStateInDB.getValue(), LinkedTreeMap.class);
        String result = GsonUtil.toJson(map.get("result"));
        ContentStateResponse contentStateResponse = GsonUtil.fromJson(result, ContentStateResponse.class);

        //update the contentsPlayedOffline in Course, only for those contents whose status is 2
        updateEnrolledCourse(contentStateRequest.getUserId(), contentStateRequest.getCourseIds().get(0), contentStateResponse);

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentStateResponse);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    private int getLeafNodeCount(Content content) {
        if (content.getChildren() == null) {
            leafNodeCount++;
        } else {
            for (Content childContent : content.getChildren()) {
                getLeafNodeCount(childContent);
            }
        }

        return leafNodeCount;
    }

    private void updateEnrolledCourse(String userId, String courseId, ContentStateResponse
            contentStateResponse) {
        //updating the course state for enrolled courses
        String enrolledCoursesKey = GET_ENROLLED_COURSES_KEY_PREFIX + userId;
        NoSqlModel enrolledCoursesInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), enrolledCoursesKey);

        if (enrolledCoursesInDB != null) {
            GenieResponse genieResponse = GsonUtil.fromJson(enrolledCoursesInDB.getValue(), GenieResponse.class);

            LinkedTreeMap map = GsonUtil.fromJson(enrolledCoursesInDB.getValue(), LinkedTreeMap.class);
            String result = GsonUtil.toJson(map.get("result"));
            EnrolledCoursesResponse enrolledCoursesResponse = GsonUtil.fromJson(result, EnrolledCoursesResponse.class);

            if (enrolledCoursesResponse != null && enrolledCoursesResponse.getCourses() != null && enrolledCoursesResponse.getCourses().size() > 0) {
                List<Course> courseList = enrolledCoursesResponse.getCourses();
                List<Course> newCourseList = new ArrayList<>(courseList);

                for (Course course : courseList) {
                    if (course.getCourseId().equalsIgnoreCase(courseId)) {

                        Course updatedCourse = course;

                        for (ContentState contentState : contentStateResponse.getContentList()) {
                            if (contentState.getStatus() == 2)
                                updatedCourse.setContentPlayedOffline(contentState.getContentId());
                        }

                        //remove old course
                        newCourseList.remove(course);

                        //add new course
                        newCourseList.add(updatedCourse);

                    }
                }

                if (newCourseList.size() > 0) {
                    //update the enrolled course response
                    enrolledCoursesResponse.setCourses(newCourseList);

                    //set it to the genie response
                    genieResponse.setResult(enrolledCoursesResponse);

                    //update the NoSQL db
                    enrolledCoursesInDB.setValue(GsonUtil.toJson(genieResponse));
                    enrolledCoursesInDB.update();
                }
            }
        }
    }
}
