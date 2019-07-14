package com.shop.data;

import com.shop.data.impl.Fulfillment;
import com.shop.data.impl.Product;
import com.shop.data.impl.Cart;
import com.shop.data.impl.User;

import java.util.List;

public interface QuickRepository {

  Cart createCart(Cart cart);

  Cart getCart(Long id);

  void deleteCart(Long id);

  Product createProduct(Product product);

  Product getProduct(String id);

  List<Product> getAllProduct();

  void deleteProduct(String id);

  User createUser(User user);

  User getUser(String user, String password);

  User getUser(String user);

  void deleteUser(Long id);

  List<Fulfillment> getAllFuilfillment();

}
