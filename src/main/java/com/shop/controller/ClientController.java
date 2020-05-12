package com.shop.controller;

import com.shop.data.impl.Cart;
import com.shop.data.impl.Fulfillment;
import com.shop.resources.*;
import com.shop.services.ClientServices;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {
    private ClientServices clientServices;

    public ClientController(ClientServices clientServices) {
        this.clientServices = clientServices;
    }

    @CrossOrigin("*")
    @GetMapping("/user")
    @ApiOperation(value = "this endpoint is used to get user info.",authorizations = {
            @Authorization(value = "Bearer")}, response = UserInfo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer <tokenHere>", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity getUser(){return clientServices.getUserInfo();}

    @CrossOrigin("*")
    @GetMapping("/user/orderHistory")
    @ApiOperation(value = "this endpoint is used to get user info.",authorizations = {
            @Authorization(value = "Bearer")}, response = UserOrderHistoryResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer <tokenHere>", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity getUserOrderHistory(){return clientServices.getOrderHistory();}

    @CrossOrigin("*")
    @GetMapping("/user/cart")
    @ApiOperation(value = "this endpoint gets a Cart", authorizations = {
            @Authorization(value = "Bearer")}, response = Cart.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer <tokenHere>", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity getCart() {
        return clientServices.getCart();
    }

    @CrossOrigin("*")
    @PatchMapping("/user/cart")
    @ApiOperation(value = "this endpoint modifies the items in a Cart", authorizations = {
            @Authorization(value = "Bearer")}, notes="if set is true then the value specified will become the new count other wise it will be added to the current running counter of items", response = BodyMessage.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer <tokenHere>", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity addRemoveItemsCart(@RequestBody CartInput items) {
        return clientServices.addRemoveItemsCart(items.getItems(),items.getSet());
    }

    @CrossOrigin("*")
    @PatchMapping("/user/cart/checkout")
    @ApiOperation(value = "this endpoint is used for checking out a Cart", authorizations = {
            @Authorization(value = "Bearer")}, response = CheckOutResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer <tokenHere>", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity checkout() {
        return clientServices.checkOut();
    }

    @CrossOrigin("*")
    @PatchMapping("/user")
    @ApiOperation(value = "this endpoint is used to update user billing infromation.",authorizations = {
            @Authorization(value = "Bearer")}, response = BodyMessage.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer <tokenHere>", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity updateBillingInfo(@RequestBody BillingInfo info){
        return clientServices.updateBillingInfo(info);
    }

}
