package com.shop.data.impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Map;
import java.util.Date;

@Entity
@Table(name = "fulfillment")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Fulfillment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn
    @JsonBackReference
    private User user;

    private String email;
    private String firstName;
    private String lastName;
    private String city;
    private String billingAddress;
    private String province;
    private String postalCode;
    private String phone;

    private Boolean fulfilled;

    @ElementCollection
    private Map<String, Long> items;
    private Float totalCost;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date updatedOn;

    public Fulfillment() {
    }

    public Fulfillment(User user, String email, String firstName, String lastName, String city, String billingAddress, String province, String postalCode, String phone, Boolean fulfilled, Map<String, Long> items, Float totalCost) {
        this.user = user;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.billingAddress = billingAddress;
        this.province = province;
        this.postalCode = postalCode;
        this.phone = phone;
        this.fulfilled = fulfilled;
        this.items = items;
        this.totalCost = totalCost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(Boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public Map<String, Long> getItems() {
        return items;
    }

    public void setItems(Map<String, Long> items) {
        this.items = items;
    }

    public Float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Float totalCost) {
        this.totalCost = totalCost;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
