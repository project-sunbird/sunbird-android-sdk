package org.ekstep.genieservices.commons.bean;

/**
 * Created on 6/6/17.
 * shriharsh
 */

public class LearnerAssessmentSummaryResponse {
    private String uid;
    private String content_id;
    private int noOfQuestions;
    private int correctAnswers;
    private Double totalTimespent;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public int getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(int noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Double getTotalTimespent() {
        return totalTimespent;
    }

    public void setTotalTimespent(Double totalTimespent) {
        this.totalTimespent = totalTimespent;
    }
}
