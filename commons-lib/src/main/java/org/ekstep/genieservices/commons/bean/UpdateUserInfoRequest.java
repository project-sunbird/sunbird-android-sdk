package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 3/23/18.
 *
 * @author Indraja
 */
public class UpdateUserInfoRequest {

    private String userId;
    private String firstName;
    private String lastName;
    private String[] language;
    private String phone;
    private boolean phoneVerified;
    private String profileSummary;
    private String[] subject;
    private String gender;
    private String dob;
    private String[] grade;
    private String location;
    private String avatar;
    private List<UserWebPages> webPages;
    private List<UserEducation> education;
    private List<UserJobProfile> jobProfile;
    private List<UserAddress> address;

    private UpdateUserInfoRequest(String userId, String firstName, String lastName, String[] language,
                                  String phone, boolean phoneVerified, String profileSummary, String[] subject, String gender,
                                  String dob, String[] grade, String location, String avatar, List<UserWebPages> webPages,
                                  List<UserEducation> education, List<UserJobProfile> jobProfile, List<UserAddress> address) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.language = language;
        this.phone = phone;
        this.phoneVerified = phoneVerified;
        this.profileSummary = profileSummary;
        this.subject = subject;
        this.gender = gender;
        this.dob = dob;
        this.grade = grade;
        this.location = location;
        this.avatar = avatar;
        this.webPages = webPages;
        this.education = education;
        this.jobProfile = jobProfile;
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String[] getLanguage() {
        return language;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public String getProfileSummary() {
        return profileSummary;
    }

    public String[] getSubject() {
        return subject;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String[] getGrade() {
        return grade;
    }

    public String getLocation() {
        return location;
    }

    public String getAvatar() {
        return avatar;
    }

    public List<UserWebPages> getWebPages() {
        return webPages;
    }

    public List<UserEducation> getEducation() {
        return education;
    }

    public List<UserJobProfile> getJobProfile() {
        return jobProfile;
    }

    public List<UserAddress> getAddress() {
        return address;
    }

    public static class Builder {

        private String userId;
        private String firstName;
        private String lastName;
        private String[] language;
        private String phone;
        private boolean phoneVerified;
        private String profileSummary;
        private String[] subject;
        private String gender;
        private String dob;
        private String[] grade;
        private String location;
        private String avatar;
        private List<UserWebPages> webPages;
        private List<UserEducation> education;
        private List<UserJobProfile> jobProfile;
        private List<UserAddress> address;

        public Builder forUser(String userId) {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalArgumentException("userId should not be null or empty.");
            }
            this.userId = userId;
            return this;
        }

        public Builder firstName(String firstName) {
            if (StringUtil.isNullOrEmpty(firstName)) {
                throw new IllegalArgumentException("firstName cannot be null or empty.");
            }

            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            if (StringUtil.isNullOrEmpty(lastName)) {
                throw new IllegalArgumentException("lastName cannot be null or empty.");
            }

            this.lastName = lastName;
            return this;
        }

        public Builder languages(String[] language) {
            if (language == null || language.length == 0) {
                throw new IllegalArgumentException("language cannot be null or empty.");
            }

            this.language = language;
            return this;
        }

        public Builder phone(String phone) {
            if (StringUtil.isNullOrEmpty(phone)) {
                throw new IllegalArgumentException("phone cannot be null or empty.");
            }

            this.phone = phone;
            return this;
        }

        public Builder profileDescription(String profileSummary) {
            this.profileSummary = profileSummary;
            return this;
        }

        public Builder subjects(String[] subject) {
            this.subject = subject;

            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder dob(String dob) {
            this.dob = dob;
            return this;
        }

        public Builder grade(String[] grade) {
            this.grade = grade;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder avatar(String avatar) {
            if (avatar == null) {
                throw new IllegalArgumentException("avatar cannot be null.");
            }
            this.avatar = avatar;
            return this;
        }

        public Builder webPages(List<UserWebPages> webPages) {
            if (webPages == null || webPages.size() == 0) {
                throw new IllegalArgumentException("web pages cannot be null or empty.");
            }

            if (this.webPages == null) {
                this.webPages = new ArrayList<>();
            }

            this.webPages.addAll(webPages);

            return this;
        }

        public Builder education(List<UserEducation> education) {
            if (education == null || education.size() == 0) {
                throw new IllegalArgumentException("education list cannot be null or empty.");
            }

            if (this.education == null) {
                this.education = new ArrayList<>();
            }

            this.education.addAll(education);

            return this;
        }

        public Builder jobProfile(List<UserJobProfile> jobProfile) {
            if (jobProfile == null || jobProfile.size() == 0) {
                throw new IllegalArgumentException("jobProfile list cannot be null or empty.");
            }

            if (this.jobProfile == null) {
                this.jobProfile = new ArrayList<>();
            }

            this.jobProfile.addAll(jobProfile);

            return this;
        }

        public Builder address(List<UserAddress> address) {
            if (address == null || address.size() == 0) {
                throw new IllegalArgumentException("address list cannot be null or empty.");
            }

            if (this.address == null) {
                this.address = new ArrayList<>();
            }

            this.address.addAll(address);

            return this;
        }


        public UpdateUserInfoRequest build() {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalStateException("userId required.");
            }

            if (!StringUtil.isNullOrEmpty(phone)) {
                this.phoneVerified = true;
            }

            return new UpdateUserInfoRequest(userId, firstName, lastName, language, phone, phoneVerified,
                    profileSummary, subject, gender, dob, grade, location, avatar, webPages,
                    education, jobProfile, address);
        }
    }
}
