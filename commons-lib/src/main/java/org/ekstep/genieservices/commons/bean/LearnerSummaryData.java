package org.ekstep.genieservices.commons.bean;

/**
 * Created on 5/6/17.
 * shriharsh
 */

public class LearnerSummaryData {

    private Long id;
    private String uid;
    private String contentId;
    private Double avgts;
    private Integer sessions;
    private Double totalts;
    private Long lastUpdated;
    private Double timespent;
    private Long timestamp;
    private String ver;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Double getAvgts() {
        return avgts;
    }

    public void setAvgts(Double avgts) {
        this.avgts = avgts;
    }

    public Integer getSessions() {
        return sessions;
    }

    public void setSessions(Integer sessions) {
        this.sessions = sessions;
    }

    public Double getTotalts() {
        return totalts;
    }

    public void setTotalts(Double totalts) {
        this.totalts = totalts;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Double getTimespent() {
        return timespent;
    }

    public void setTimespent(Double timespent) {
        this.timespent = timespent;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
