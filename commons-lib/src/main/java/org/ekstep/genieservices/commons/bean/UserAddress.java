package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 29/3/18.
 * shriharsh
 */
public class UserAddress {

    private String addType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private boolean isDeleted;
    private String id;

    private UserAddress(String addType, String addressLine1, String addressLine2, String city,
                        String state, String country, String zipcode, boolean isDeleted, String id) {
        this.addType = addType;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
        this.isDeleted = isDeleted;
        this.id = id;
    }

    public String getAddType() {
        return addType;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getId() {
        return id;
    }

    public static class Builder {
        private String addType;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String country;
        private String zipcode;
        private boolean isDeleted;
        private String id;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder addressType(String addType) {
            if (StringUtil.isNullOrEmpty(addType)) {
                throw new IllegalArgumentException("address type cannot be null or empty.");
            }

            this.addType = addType;
            return this;
        }

        public Builder addressLine1(String addressLine1) {
            if (StringUtil.isNullOrEmpty(addressLine1)) {
                throw new IllegalArgumentException("address type cannot be null or empty.");
            }

            this.addressLine1 = addressLine1;
            return this;
        }

        public Builder addressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public Builder city(String city) {
            if (StringUtil.isNullOrEmpty(city)) {
                throw new IllegalArgumentException("city cannot be null or empty.");
            }

            this.city = city;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder zipCode(String zipcode) {
            this.zipcode = zipcode;
            return this;
        }

        public Builder deleteAddress() {
            this.isDeleted = true;
            return this;
        }

        public UserAddress build() {
            if (StringUtil.isNullOrEmpty(addType)) {
                throw new IllegalStateException("address type is required.");
            }

            if (StringUtil.isNullOrEmpty(addressLine1)) {
                throw new IllegalStateException("addressLine1 is required");
            }

            return new UserAddress(addType, addressLine1, addressLine2, city, state, country, zipcode, isDeleted, id);
        }
    }

}
