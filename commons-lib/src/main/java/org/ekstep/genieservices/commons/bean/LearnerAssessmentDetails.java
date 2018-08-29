package org.ekstep.genieservices.commons.bean;

/**
 * This class holds details about learner assessment.
 *
 */
public class LearnerAssessmentDetails {

    private Long id = -1L;
    private String uid;
    private String contentId;
    private String qid;
    private Double qindex;
    private Integer correct;
    private Double score;
    private Double timespent;
    private String res;
    private Long timestamp;
    private String qdesc;
    private String qtitle;
    private Double maxScore;
    private String hierarchyData;
    private Double total_ts;

    public Double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    public String getHierarchyData() {
        return hierarchyData;
    }

    public void setHierarchyData(String hierarchyData) {
        this.hierarchyData = hierarchyData;
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

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public Double getQindex() {
        return qindex;
    }

    public void setQindex(Double qindex) {
        this.qindex = qindex;
    }

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getTimespent() {
        return timespent;
    }

    public void setTimespent(Double timespent) {
        this.timespent = timespent;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getQdesc() {
        return qdesc;
    }

    public void setQdesc(String qdesc) {
        this.qdesc = qdesc;
    }

    public String getQtitle() {
        return qtitle;
    }

    public void setQtitle(String qtitle) {
        this.qtitle = qtitle;
    }

    public Double getTotal_ts() {
        return total_ts;
    }

    public void setTotal_ts(Double total_ts) {
        this.total_ts = total_ts;
    }


}
