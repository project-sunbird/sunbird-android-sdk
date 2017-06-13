package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created on 6/6/17.
 * shriharsh
 */

public class LearnerAssessmentSummary implements Serializable {
    private String uid;
    private String contentId;
    private int noOfQuestions;
    private int correctAnswers;
    private Double totalTimespent;

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
