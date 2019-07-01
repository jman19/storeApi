package com.shop.resources;

import java.util.Map;

public class CartInput {

  private String id;
  private Map<String, Long> items;
  private Float totalCost;

  public CartInput() {
  }

  public CartInput(String id, Map<String, Long> items, Float totalCost) {
    this.id = id;
    this.items = items;
    this.totalCost = totalCost;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
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
