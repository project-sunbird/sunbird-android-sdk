package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.Arrays;
import java.util.Map;

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
    private String[] board;
    private String[] contentType;
    private String[] domain;
    private Map<String, Integer> compatibilityLevel;
    private String dialcodes;

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

    public String[] getBoard() {
        return board;
    }

    public void setBoard(String[] board) {
        this.board = board;
    }

    public String[] getContentType() {
        return contentType;
    }

    public void setContentType(String[] contentType) {
        this.contentType = contentType;
    }

    public String[] getDomain() {
        return domain;
    }

    public void setDomain(String[] domain) {
        this.domain = domain;
    }

    public Map<String, Integer> getCompatibilityLevel() {
        return compatibilityLevel;
    }

    public void setCompatibilityLevel(Map<String, Integer> compatibilityLevel) {
        this.compatibilityLevel = compatibilityLevel;
    }

    public String getDialcodes() {
        return dialcodes;
    }

    public void setDialcodes(String dialcodes) {
        this.dialcodes = dialcodes;
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

        if (!CollectionUtil.isEmpty(board)) {
            Arrays.sort(board);
            builder.append(StringUtil.join(",", board));
        }

        if (!CollectionUtil.isEmpty(contentType)) {
            Arrays.sort(contentType);
            builder.append(StringUtil.join(",", contentType));
        }

        if (!CollectionUtil.isEmpty(domain)) {
            Arrays.sort(domain);
            builder.append(StringUtil.join(",", domain));
        }

        if (!StringUtil.isNullOrEmpty(dialcodes)) {
            builder.append(dialcodes);
        }

        return builder.toString();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
