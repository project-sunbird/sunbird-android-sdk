package org.ekstep.genieservices.commons.bean;

import com.google.gson.Gson;

import org.ekstep.genieservices.commons.IValidate;
import org.ekstep.genieservices.commons.bean.validator.DateValidator;
import org.ekstep.genieservices.commons.bean.validator.StringValidator;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Profile implements IValidate {
    private String uid;
    private String handle = "";
    private String avatar = "";
    private String gender;
    private int age = -1;
    private int day = -1;
    private int month = -1;
    private int standard = -1;
    private String language = "";
    private String medium = "";
    private String board = "";
    private boolean isGroupUser;
    private Date createdAt;
    private transient List<IValidate> validators;

    public Profile(String handle, String avatar, String language) {
        this.handle = handle;
        this.avatar = avatar;
        this.language = language;
        this.createdAt = new Date();
    }

    public Profile(String handle, String avatar, String language, int standard, String medium) {
        this.handle = handle;
        this.avatar = avatar;
        this.language = language;
        this.standard = standard;
        this.medium = medium;
        this.createdAt = new Date();
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
        int year = this.age == -1 ? Calendar.getInstance().get(1) : Calendar.getInstance().get(1) - this.age;
        return year;
    }

    public boolean isGroupUser() {
        return this.isGroupUser;
    }

    public void setGroupUser(boolean groupUser) {
        this.isGroupUser = groupUser;
    }

    public String getBoard() {
        return this.board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getMedium() {
        return this.medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public boolean isValid() {
        this.initializeValidation();
        Boolean isValid = Boolean.valueOf(true);

        IValidate validator;
        for(Iterator var2 = this.validators.iterator(); var2.hasNext(); isValid = Boolean.valueOf(isValid.booleanValue() & validator.isValid())) {
            validator = (IValidate)var2.next();
        }

        return isValid.booleanValue();
    }

    private void initializeValidation() {
        HashMap validationStringFields = new HashMap();
        validationStringFields.put("handle", this.handle);
        validationStringFields.put("avatar", this.avatar);
        validationStringFields.put("language", this.language);
        StringValidator stringValidator = new StringValidator(validationStringFields);
        DayMonth dayMonth = new DayMonth(this.day, this.month, this.getYear());
        HashMap validationDateFields = new HashMap();
        validationDateFields.put("daymonth", dayMonth);
        DateValidator dateValidator = new DateValidator(validationDateFields);
        this.validators = Arrays.asList(new IValidate[]{stringValidator, dateValidator});
    }

    @Override
    public List<String> getErrors() {
        return null;
    }
}
