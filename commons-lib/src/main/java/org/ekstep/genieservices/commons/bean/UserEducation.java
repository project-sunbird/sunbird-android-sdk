package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 29/3/18.
 * shriharsh
 */
public class UserEducation {

    private String id;
    private String degree;
    private String name;
    private String grade;
    private int yearOfPassing;
    private double percentage;
    private String boardOrUniversity;
    private boolean isDeleted;

    private UserEducation(String id, String degree, String name, String grade, int yearOfPassing,
                          double percentage, String boardOrUniversity, boolean isDeleted) {
        this.id = id;
        this.degree = degree;
        this.name = name;
        this.grade = grade;
        this.yearOfPassing = yearOfPassing;
        this.percentage = percentage;
        this.boardOrUniversity = boardOrUniversity;
        this.isDeleted = isDeleted;
    }

    public String getDegree() {
        return degree;
    }

    public String getName() {
        return name;
    }

    public int getYearOfPassing() {
        return yearOfPassing;
    }

    public double getPercentage() {
        return percentage;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getGrade() {
        return grade;
    }

    public String getBoardOrUniversity() {
        return boardOrUniversity;
    }

    public String getId() {
        return id;
    }

    public static class Builder {
        private String id;
        private String degree;
        private String name;
        private String grade;
        private int yearOfPassing;
        private double percentage;
        private String boardOrUniversity;
        private boolean isDeleted;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder degree(String degree) {
            if (StringUtil.isNullOrEmpty(degree)) {
                throw new IllegalArgumentException("degree cannot be null or empty.");
            }
            this.degree = degree;
            return this;
        }

        public Builder institutionName(String name) {
            if (StringUtil.isNullOrEmpty(name)) {
                throw new IllegalArgumentException("name cannot be null or empty.");
            }
            this.name = name;
            return this;
        }

        public Builder yearOfPassing(int yearOfPassing) {
            this.yearOfPassing = yearOfPassing;
            return this;
        }

        public Builder percentage(double percentage) {
            this.percentage = percentage;
            return this;
        }

        public Builder deleteEducation() {
            this.isDeleted = true;
            return this;
        }

        public Builder grade(String grade) {
            this.grade = grade;
            return this;
        }

        public Builder boardOrUniversity(String boardOrUniversity) {
            this.boardOrUniversity = boardOrUniversity;
            return this;
        }

        public UserEducation build() {
            if (StringUtil.isNullOrEmpty(degree)) {
                throw new IllegalStateException("degree is required.");
            }

            if (StringUtil.isNullOrEmpty(name)) {
                throw new IllegalStateException("Institution name is required");
            }

            return new UserEducation(id, degree, name, grade, yearOfPassing, percentage, boardOrUniversity, isDeleted);
        }

    }
}
