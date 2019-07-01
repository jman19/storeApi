package com.shop.data.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Cart")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonIgnore
  private Long id;

  @ElementCollection
  private Map<String, Long> items;
  private Float totalCost;

  public Cart() {
    items = new HashMap<String, Long>();
    totalCost = (float) 0;
  }

  public Cart(Map<String, Long> items, Float totalCost) {
    this.items = items;
    this.totalCost = totalCost;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

}
