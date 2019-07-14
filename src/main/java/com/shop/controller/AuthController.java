package com.shop.controller;

import com.shop.resources.LoginInput;
import com.shop.resources.LoginResponse;
import com.shop.resources.SignUpInput;
import com.shop.services.AuthServices;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private AuthServices authServices;

    public AuthController(AuthServices authServices) {
        this.authServices = authServices;
    }

    @CrossOrigin("*")
    @PostMapping("/auth/login")
    @ApiOperation(value = "this endpoint is used to login. returns a jwt token to be used by other services", response = LoginResponse.class)
    public ResponseEntity login(@RequestBody LoginInput input) throws Exception{
        return authServices.login(input);
    }

    @CrossOrigin("*")
    @PostMapping("/auth/signUp")
    @ApiOperation(value = "this endpoint is used to create a new account. returns a jwt token to be used by other services", response = LoginResponse.class)
    public ResponseEntity signUp(@RequestBody SignUpInput input) throws Exception {
        return authServices.createAccount(input);
    }
}
