package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * This class holds the details about assessment summary of a learner.
 */
public class LearnerAssessmentSummary implements Serializable {
    private String uid;
    private String contentId;
    private int noOfQuestions;
    private int correctAnswers;
    private Double totalTimespent;
    private String hierarchyData;
    private Double totalMaxScore;
    private Double totalScore;

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

    public String getHierarchyData() {
        return hierarchyData;
    }

    public void setHierarchyData(String hierarchyData) {
        this.hierarchyData = hierarchyData;
    }

    public Double getTotalMaxScore() {
        return totalMaxScore;
    }

    public void setTotalMaxScore(Double totalMaxScore) {
        this.totalMaxScore = totalMaxScore;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }
}
