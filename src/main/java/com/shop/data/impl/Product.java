package com.shop.data.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

  @Id
  private String title;
  private Float price;
  private Long inventoryCount;

  public Product() {
  }

  public Product(String title, Float price, Long inventoryCount) {
    this.title = title;
    this.price = price;
    this.inventoryCount = inventoryCount;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public Long getInventoryCount() {
    return inventoryCount;
  }

  public void setInventoryCount(Long inventoryCount) {
    this.inventoryCount = inventoryCount;
  }
}
