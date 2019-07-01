package com.shop.resources;

public class BodyMessage {

  private String message;
  private Integer code;

  public BodyMessage() {
  }

  public BodyMessage(String message, Integer code) {
    this.message = message;
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }
}
