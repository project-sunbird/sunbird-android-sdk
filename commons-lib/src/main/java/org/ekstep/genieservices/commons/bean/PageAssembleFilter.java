package org.ekstep.genieservices.commons.bean;

/**
 * Created by souvikmondal on 21/3/18.
 */

public class PageAssembleFilter {

    private String[] subject;
    private String[] medium;
    private String[] gradeLevel;
    private String[] ageGroup;
    private String[] language;
    private String[] ownership;
    private String[] concepts;

    public String[] getSubject() {
        return subject;
    }

    public void setSubject(String[] subject) {
        this.subject = subject;
    }

    public String[] getMedium() {
        return medium;
    }

    public void setMedium(String[] medium) {
        this.medium = medium;
    }

    public String[] getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String[] gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String[] getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String[] ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String[] getLanguage() {
        return language;
    }

    public void setLanguage(String[] language) {
        this.language = language;
    }

    public String[] getOwnership() {
        return ownership;
    }

    public void setOwnership(String[] ownership) {
        this.ownership = ownership;
    }

    public String[] getConcepts() {
        return concepts;
    }

    public void setConcepts(String[] concepts) {
        this.concepts = concepts;
    }
}
