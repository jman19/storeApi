package com.shop.services;

import com.shop.data.impl.Product;
import com.shop.data.QuickRepository;
import com.shop.resources.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShopServices {

  private QuickRepository quickRepository;

  public ShopServices(QuickRepository quickRepository) {
    this.quickRepository = quickRepository;
  }

  public ResponseEntity getProduct(String id) {
    Product product = quickRepository.getProduct(id);
    if (product == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new BodyMessage("product not found", HttpStatus.NOT_FOUND.value()));
    } else {
      return ResponseEntity.ok(product);
    }
  }

  public ResponseEntity getAllProducts(Boolean hideOutOfStock) {
    List<Product> productList = quickRepository.getAllProduct();
    if (hideOutOfStock) {
      List<Product> inStockProductList = new ArrayList<Product>();
      for (Product product : productList) {
        if (product.getInventoryCount() != 0) {
          inStockProductList.add(product);
        }
      }
      return ResponseEntity.ok(new ProductList(inStockProductList));
    }
    return ResponseEntity.ok(new ProductList(productList));
  }

}
