package com.shop.data;

import com.shop.data.impl.Product;
import com.shop.data.impl.Cart;
import com.shop.data.impl.User;

import java.util.List;

public interface QuickRepository {

  public Cart createCart(Cart cart);

  public Cart getCart(Long id);

  public void deleteCart(Long id);

  public Product createProduct(Product product);

  public Product getProduct(String id);

  public List<Product> getAllProduct();

  public void deleteProduct(String id);

  public User createUser(User user);

  public User getUser(String user, String password);

  public User getUser(String user);

  public void deleteUser(Long id);

}
