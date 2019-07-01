package com.shop.resources;

public class ProductUpdateInput {

  private Long inventoryCount;
  private Float price;

  public ProductUpdateInput() {
  }

  public ProductUpdateInput(Long inventoryCount, Float price) {
    this.inventoryCount = inventoryCount;
    this.price = price;
  }

  public Long getInventoryCount() {
    return inventoryCount;
  }

  public void setInventoryCount(Long inventoryCount) {
    this.inventoryCount = inventoryCount;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }
}
