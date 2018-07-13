package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.Date;

/**
 * This class holds all the data related to a group.
 */
public class Group {

    private String gid;
    private String name;
    private String[] syllabus;
    private String[] grade;
    private Date createdAt;
    private String[] uids;

    public Group(String name) {
        this.name = name;
        this.createdAt = DateUtil.now();
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String[] syllabus) {
        this.syllabus = syllabus;
    }

    public String[] getGrade() {
        return grade;
    }

    public void setGrade(String[] grade) {
        this.grade = grade;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String[] getUids() {
        return uids;
    }

    public void setUids(String[] uids) {
        this.uids = uids;
    }
}
