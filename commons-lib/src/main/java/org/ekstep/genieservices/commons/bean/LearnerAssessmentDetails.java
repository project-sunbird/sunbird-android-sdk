package org.ekstep.genieservices.commons.bean;

/**
 * Created on 4/6/17.
 * shriharsh
 */

public class LearnerAssessmentDetails {

    private Long id;
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
}
