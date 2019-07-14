package com.shop.data.impl;

import com.shop.data.QuickRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class QuickSpringRepository implements QuickRepository {

  private CartJPA cartJpa;
  private ProductJPA productJpa;
  private UserJPA userJPA;
  private FulfillmentJPA fulfillmentJPA;

  public QuickSpringRepository(CartJPA cart, ProductJPA product, UserJPA user, FulfillmentJPA fulfillmentJPA) {
    cartJpa = cart;
    productJpa = product;
    userJPA = user;
    fulfillmentJPA=fulfillmentJPA;
  }

  public Cart createCart(Cart cart) {
    return cartJpa.save(cart);
  }

  public Cart getCart(Long id) {
    try {
      Optional byId = cartJpa.findById(id);
      return (Cart) byId.get();
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  public void deleteCart(Long id) {
    cartJpa.deleteById(id);
  }

  public Product createProduct(Product product) {
    return productJpa.save(product);
  }

  public Product getProduct(String id) {
    try {
      Optional byId = productJpa.findById(id);
      return (Product) byId.get();
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  public List<Product> getAllProduct() {
    return productJpa.findAll();
  }

  public void deleteProduct(String id) {
    productJpa.deleteById(id);
  }

  public User createUser(User user) {
    return userJPA.save(user);
  }

  public User getUser(String user, String password) {
    return userJPA.findByPasswordAndUser(password, user);
  }

  public User getUser(String user) {
    return userJPA.findByUser(user);
  }

  public void deleteUser(Long id) {
    userJPA.deleteById(id);
  }

  public List<Fulfillment> getAllFuilfillment() {return fulfillmentJPA.findAll();}
}
