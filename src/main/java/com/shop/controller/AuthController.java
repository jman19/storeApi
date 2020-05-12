package com.shop.controller;

import com.shop.resources.*;
import com.shop.services.AuthServices;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @CrossOrigin("*")
    @PatchMapping("/auth")
    @ApiOperation(value = "this endpoint is used to update user creds returns new jwt for use.",authorizations = {
            @Authorization(value = "Bearer")}, response = LoginResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer <tokenHere>", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity updateCred(@RequestBody UpdateAuthInput updateAuthInput)throws Exception{
        return authServices.updateAuth(updateAuthInput);
    }
}
