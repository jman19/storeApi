package com.shop.controller;

import com.shop.data.impl.Product;
import com.shop.data.impl.Cart;
import com.shop.resources.*;
import com.shop.services.ShopServices;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ShopController {

  private ShopServices shopServices;

  public ShopController(com.shop.services.ShopServices shopServices) {
    this.shopServices = shopServices;
  }


  @CrossOrigin("*")
  @PostMapping("/login")
  @ApiOperation(value = "this endpoint is used to login. returns a jwt token to be used by other services", response = LoginResponse.class)
  public ResponseEntity login(@RequestBody LoginInput input) {
    return shopServices.login(input);
  }

  @CrossOrigin("*")
  @PostMapping("/signUp")
  @ApiOperation(value = "this endpoint is used to create a new account. returns a jwt token to be used by other services", response = LoginResponse.class)
  public ResponseEntity signUp(@RequestBody LoginInput input) {
    return shopServices.createAccount(input);
  }

  @CrossOrigin("*")
  @GetMapping("/cart")
  @ApiOperation(value = "this endpoint gets a Cart", authorizations = {
      @Authorization(value = "Bearer")}, response = Cart.class)
  @ApiImplicitParams({
      @ApiImplicitParam(name = "Authorization", value = "Bearer <tokenHere>", required = true, dataType = "string", paramType = "header")})
  public ResponseEntity getCart() {
    return shopServices.getCart();
  }

  @CrossOrigin("*")
  @PatchMapping("/cart")
  @ApiOperation(value = "this endpoint modifies the items in a Cart", authorizations = {
      @Authorization(value = "Bearer")}, response = BodyMessage.class)
  @ApiImplicitParams({
      @ApiImplicitParam(name = "Authorization", value = "Bearer <tokenHere>", required = true, dataType = "string", paramType = "header")})
  public ResponseEntity addRemoveItemsCart(@RequestBody Map<String, Long> items) {
    return shopServices.addRemoveItemsCart(items);
  }

  @PatchMapping("/cart/checkout")
  @ApiOperation(value = "this endpoint is used for checking out a Cart", authorizations = {
      @Authorization(value = "Bearer")}, response = CheckOutResponse.class)
  @ApiImplicitParams({
      @ApiImplicitParam(name = "Authorization", value = "Bearer <tokenHere>", required = true, dataType = "string", paramType = "header")})
  public ResponseEntity checkout() {
    return shopServices.checkOut();
  }

  @PostMapping("/product")
  @ApiOperation(value = "this endpoint creates a new product for the store", response = Product.class, authorizations = {
      @Authorization(value = "basicAuth")})
  public ResponseEntity createProduct(@RequestBody Product product) {
    return shopServices.createProduct(product);
  }

  @GetMapping("/product/{name}")
  @ApiOperation(value = "this endpoint gets a product's details", response = Product.class)
  public ResponseEntity getProduct(@PathVariable String name) {
    return shopServices.getProduct(name);
  }

  @CrossOrigin("*")
  @GetMapping("/product")
  @ApiOperation(value = "this endpoint gets all product's details", response = ProductList.class)
  public ResponseEntity getAllProduct(@RequestParam("hideOutOfStock") Boolean hideOutOfStock) {
    return shopServices.getAllProducts(hideOutOfStock);
  }

  @PutMapping("/product/{name}")
  @ApiOperation(value = "this endpoint allows one to update a product's details", response = BodyMessage.class, authorizations = {
      @Authorization(value = "basicAuth")})
  public ResponseEntity updateProduct(@PathVariable String name,
      @RequestBody ProductUpdateInput input) {
    return shopServices.updateProduct(name, input);
  }

  @DeleteMapping("/product/{name}")
  @ApiOperation(value = "This endpoint deletes a product from store", response = BodyMessage.class, authorizations = {
      @Authorization(value = "basicAuth")})
  public ResponseEntity deleteProduct(@PathVariable String name) {
    return shopServices.deleteProduct(name);
  }
}
