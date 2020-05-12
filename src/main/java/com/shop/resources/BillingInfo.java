package com.shop.resources;

public class BillingInfo {
    private String firstName;
    private String lastName;
    private String city;
    private String billingAddress;
    private String province;
    private String postalCode;

    public BillingInfo(){}

    public BillingInfo(String firstName, String lastName, String city, String billingAddress, String province, String postalCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.billingAddress = billingAddress;
        this.province = province;
        this.postalCode = postalCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
