package com.shop.resources;

import com.shop.data.impl.Product;

import java.util.List;

public class ProductList {
    private List<Product> products;

    public ProductList() {
    }

    public ProductList(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
