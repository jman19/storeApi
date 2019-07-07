package com.shop.resources;

import java.util.Map;

public class CartInput {

  private Map<String, Long> items;
  private Boolean set;

  public CartInput() {
  }

  public CartInput(Map<String, Long> items, Boolean set) {
    this.items = items;
    this.set = set;
  }

  public Map<String, Long> getItems() {
    return items;
  }

  public void setItems(Map<String, Long> items) {
    this.items = items;
  }

  public Boolean getSet() {
    return set;
  }

  public void setSet(Boolean set) {
    this.set = set;
  }
}
