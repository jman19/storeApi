package com.shop.resources;

public class LoginResponse {
    private String jwt;
    private Long expires;
    private Integer code;

    public LoginResponse() {
    }

    public LoginResponse(String jwt, Long expires, Integer code) {
        this.jwt = jwt;
        this.expires = expires;
        this.code = code;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
