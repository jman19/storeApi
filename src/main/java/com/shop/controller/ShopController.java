package com.shop.controller;

import com.shop.data.impl.Fulfillment;
import com.shop.data.impl.Product;
import com.shop.data.impl.Cart;
import com.shop.data.impl.User;
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
  @GetMapping("/shop/product/{name}")
  @ApiOperation(value = "this endpoint gets a product's details", response = Product.class)
  public ResponseEntity getProduct(@PathVariable String name) {
    return shopServices.getProduct(name);
  }

  @CrossOrigin("*")
  @GetMapping("/shop/product")
  @ApiOperation(value = "this endpoint gets all product's details", response = ProductList.class)
  public ResponseEntity getAllProduct(@RequestParam("hideOutOfStock") Boolean hideOutOfStock) {
    return shopServices.getAllProducts(hideOutOfStock);
  }

}
