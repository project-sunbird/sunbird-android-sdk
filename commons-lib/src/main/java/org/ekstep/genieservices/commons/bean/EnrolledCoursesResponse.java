package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.List;

/**
 * Created on 9/3/18.
 *
 * @author anil
 */
public class EnrolledCoursesResponse {

    private List<Course> courses;

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
