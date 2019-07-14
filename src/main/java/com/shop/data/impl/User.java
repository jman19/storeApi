package com.shop.data.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "User")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String user;
  private String password;
  private String firstName;
  private String lastName;
  private String city;
  private String billingAddress;
  private String province;
  private String postalCode;
  private String phone;

  @OneToOne(cascade = CascadeType.REMOVE)
  private Cart cart;

  @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
  private List<Fulfillment> fulfillment;

  public User() {
  }

  public User(String user, String password, String firstName, String lastName, String city, String billingAddress, String province, String postalCode, String phone, Cart cart) {
    this.user = user;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.city = city;
    this.billingAddress = billingAddress;
    this.province = province;
    this.postalCode = postalCode;
    this.phone = phone;
    this.cart = cart;
  }

  public User(String user, String password, String firstName, String lastName, String city, String billingAddress, String province, String postalCode, String phone, Cart cart, List<Fulfillment> fulfillment) {
    this.user = user;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.city = city;
    this.billingAddress = billingAddress;
    this.province = province;
    this.postalCode = postalCode;
    this.phone = phone;
    this.cart = cart;
    this.fulfillment = fulfillment;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public List<Fulfillment> getFulfillment() {
    return fulfillment;
  }

  public void setFulfillment(List<Fulfillment> fulfillment) {
    this.fulfillment = fulfillment;
  }
}
