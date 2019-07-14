package com.shop.services;

import com.shop.data.QuickRepository;
import com.shop.data.impl.Product;
import com.shop.resources.BodyMessage;
import com.shop.resources.ProductUpdateInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServices {
    private QuickRepository quickRepository;

    public EmployeeServices(QuickRepository quickRepository) {
        this.quickRepository = quickRepository;
    }

    public ResponseEntity createProduct(Product product) {
        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BodyMessage("product must not be null", HttpStatus.BAD_REQUEST.value()));
        }
        if (quickRepository.getProduct(product.getTitle()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new BodyMessage("product already exists", HttpStatus.CONFLICT.value()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(quickRepository.createProduct(product));
    }

    public ResponseEntity deleteProduct(String id) {
        quickRepository.deleteProduct(id);
        return ResponseEntity.ok(new BodyMessage("successfully deleted", HttpStatus.OK.value()));
    }

    public ResponseEntity updateProduct(String id, ProductUpdateInput product) {
        Product productToUpdate = quickRepository.getProduct(id);
        if (productToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BodyMessage("product not found", HttpStatus.NOT_FOUND.value()));
        }
        //update product
        productToUpdate.setInventoryCount(product.getInventoryCount());
        productToUpdate.setPrice(product.getPrice());
        quickRepository.createProduct(productToUpdate);

        return ResponseEntity.ok(new BodyMessage("product details updated", HttpStatus.OK.value()));
    }
}
