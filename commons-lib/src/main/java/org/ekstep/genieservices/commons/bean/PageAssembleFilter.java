package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.Arrays;

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (!CollectionUtil.isEmpty(subject)) {
            Arrays.sort(subject);
            builder.append(StringUtil.join(",", subject));
        }

        if (!CollectionUtil.isEmpty(medium)) {
            Arrays.sort(medium);
            builder.append(StringUtil.join(",", medium));
        }

        if (!CollectionUtil.isEmpty(gradeLevel)) {
            Arrays.sort(gradeLevel);
            builder.append(StringUtil.join(",", gradeLevel));
        }

        if (!CollectionUtil.isEmpty(ageGroup)) {
            Arrays.sort(ageGroup);
            builder.append(StringUtil.join(",", ageGroup));
        }

        if (!CollectionUtil.isEmpty(language)) {
            Arrays.sort(language);
            builder.append(StringUtil.join(",", language));
        }

        if (!CollectionUtil.isEmpty(ownership)) {
            Arrays.sort(ownership);
            builder.append(StringUtil.join(",", ownership));
        }

        if (!CollectionUtil.isEmpty(concepts)) {
            Arrays.sort(concepts);
            builder.append(StringUtil.join(",", concepts));
        }

        return builder.toString();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
