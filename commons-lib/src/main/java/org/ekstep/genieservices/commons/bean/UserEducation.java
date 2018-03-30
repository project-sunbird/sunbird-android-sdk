package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 29/3/18.
 * shriharsh
 */
public class UserEducation {

    private String degree;
    private String name;
    private int yearOfPassing;
    private double percentage;
    private boolean isDeleted;
    private String grade;
    private String boardOrUniversity;
    private String id;

    public UserEducation(String degree, String name, int yearOfPassing, double percentage, boolean isDeleted, String grade, String boardOrUniversity, String id) {
        this.degree = degree;
        this.name = name;
        this.yearOfPassing = yearOfPassing;
        this.percentage = percentage;
        this.isDeleted = isDeleted;
        this.grade = grade;
        this.boardOrUniversity = boardOrUniversity;
        this.id = id;
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
        private String degree;
        private String name;
        private int yearOfPassing;
        private double percentage;
        private boolean isDeleted;
        private String grade;
        private String boardOrUniversity;
        private String id;

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

        public Builder isDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
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

            return new UserEducation(degree, name, yearOfPassing, percentage, isDeleted, grade, boardOrUniversity, id);
        }

    }
}
