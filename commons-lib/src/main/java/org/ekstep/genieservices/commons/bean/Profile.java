package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.IValidate;
import org.ekstep.genieservices.commons.bean.enums.ProfileType;
import org.ekstep.genieservices.commons.bean.enums.UserSource;
import org.ekstep.genieservices.commons.bean.validator.DateValidator;
import org.ekstep.genieservices.commons.bean.validator.StringValidator;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class holds all the data related to profile details.
 */
public class Profile implements Serializable, IValidate {

    private String uid;
    private String handle = "";
    private String avatar = "";
    private String gender;
    private int age = -1;
    private int day = -1;
    private int month = -1;
    private int standard = -1;
    private String language = "";
    private String[] medium;
    private String[] board;
    private boolean isGroupUser;
    private Date createdAt;
    private transient List<IValidate> validators;
    private String profileImage = "";
    private ProfileType profileType;
    private String[] subject;
    private String[] grade;
    private String[] syllabus;
    private UserSource source;

    public Profile(String uid) {
        this.uid = uid;
    }

    public Profile(String handle, String avatar, String language) {
        this.handle = handle;
        this.avatar = avatar;
        this.language = language;
        this.createdAt = DateUtil.now();
    }

    public Profile(String handle, String avatar, String language, int standard, String[] medium) {
        this.handle = handle;
        this.avatar = avatar;
        this.language = language;
        this.standard = standard;
        this.medium = medium;
        this.createdAt = DateUtil.now();
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHandle() {
        return this.handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getStandard() {
        return this.standard;
    }

    public void setStandard(int standard) {
        this.standard = standard;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getYear() {
        int year = this.age == -1 ? Calendar.getInstance().get(Calendar.YEAR) : Calendar.getInstance().get(Calendar.YEAR) - this.age;
        return year;
    }

    public boolean isGroupUser() {
        return this.isGroupUser;
    }

    public void setGroupUser(boolean groupUser) {
        this.isGroupUser = groupUser;
    }

    public String[] getBoard() {
        return this.board;
    }

    public void setBoard(String[] board) {
        this.board = board;
    }

    public String[] getMedium() {
        return this.medium;
    }

    public void setMedium(String[] medium) {
        this.medium = medium;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileType() {
        return profileType != null ? profileType.getValue() : "";
    }

    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }

    public String[] getSubject() {
        return subject;
    }

    public void setSubject(String[] subject) {
        this.subject = subject;
    }

    public String[] getGrade() {
        return grade;
    }

    public void setGrade(String[] grade) {
        this.grade = grade;
    }

    public String[] getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String[] syllabus) {
        this.syllabus = syllabus;
    }

    public UserSource getSource() {
        return source;
    }

    public void setSource(UserSource source) {
        this.source = source;
    }

    public String toString() {
        return GsonUtil.toJson(this);
    }

    @Override
    public boolean isValid() {
        this.initializeValidation();
        boolean isValid = true;

        IValidate validator;
        for (Iterator var2 = this.validators.iterator(); var2.hasNext(); isValid = (isValid & validator.isValid())) {
            validator = (IValidate) var2.next();
        }

        return isValid;
    }

    private void initializeValidation() {
        Map<String, String> validationStringFields = new HashMap<>();
        validationStringFields.put("handle", this.handle);
        validationStringFields.put("avatar", this.avatar);
        validationStringFields.put("language", this.language);
        StringValidator stringValidator = new StringValidator(validationStringFields);

        DayMonth dayMonth = new DayMonth(this.day, this.month, this.getYear());
        Map<String, DayMonth> validationDateFields = new HashMap<>();
        validationDateFields.put("daymonth", dayMonth);
        DateValidator dateValidator = new DateValidator(validationDateFields);
        this.validators = Arrays.asList(stringValidator, dateValidator);
    }

    @Override
    public List<String> getErrors() {
        List<String> errors = new ArrayList<>();
        for (IValidate validator : this.validators) {
            errors.addAll(validator.getErrors());
        }
        return errors;
    }
}
