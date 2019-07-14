package com.shop.controller;

import com.shop.data.impl.Product;
import com.shop.resources.BodyMessage;
import com.shop.resources.ProductUpdateInput;
import com.shop.services.EmployeeServices;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private EmployeeServices employeeServices;

    public EmployeeController(EmployeeServices employeeServices) {
        this.employeeServices = employeeServices;
    }

    @PutMapping("/employee/product/{name}")
    @ApiOperation(value = "this endpoint allows one to update a product's details", response = BodyMessage.class, authorizations = {
            @Authorization(value = "basicAuth")})
    public ResponseEntity updateProduct(@PathVariable String name,
                                        @RequestBody ProductUpdateInput input) {
        return employeeServices.updateProduct(name, input);
    }

    @DeleteMapping("/employee/product/{name}")
    @ApiOperation(value = "This endpoint deletes a product from store", response = BodyMessage.class, authorizations = {
            @Authorization(value = "basicAuth")})
    public ResponseEntity deleteProduct(@PathVariable String name) {
        return employeeServices.deleteProduct(name);
    }


    @PostMapping("/employee/product")
    @ApiOperation(value = "this endpoint creates a new product for the store", response = Product.class, authorizations = {
            @Authorization(value = "basicAuth")})
    public ResponseEntity createProduct(@RequestBody Product product) {
        return employeeServices.createProduct(product);
    }
}
