package com.shop.resources;

public class CheckOutResponse {

  private String message;
  private Float total;

  public CheckOutResponse() {
  }

  public CheckOutResponse(String message, Float total) {
    this.message = message;
    this.total = total;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Float getTotal() {
    return total;
  }

  public void setTotal(Float total) {
    this.total = total;
  }
}
