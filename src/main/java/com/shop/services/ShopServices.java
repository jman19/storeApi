package com.shop.services;

import com.shop.data.impl.Cart;
import com.shop.data.impl.Product;
import com.shop.data.QuickRepository;
import com.shop.data.impl.User;
import com.shop.resources.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class ShopServices {

  private QuickRepository quickRepository;
  private Environment env;
  @Autowired
  private HttpServletRequest request;

  public ShopServices(QuickRepository quickRepository, Environment env) {
    this.quickRepository = quickRepository;
    this.env = env;
  }

  public ResponseEntity createAccount(LoginInput form) {
    try {
      Long expire=System.currentTimeMillis() + 3600000;
      String hash = DigestUtils.sha256Hex(form.getPassword());
      User user = quickRepository.getUser(form.getEmail());
      if (user != null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            new BodyMessage("A user with that name already exist", HttpStatus.CONFLICT.value()));
      }
      user = new User();
      user.setCart(quickRepository.createCart(new Cart()));
      user.setUser(form.getEmail());
      user.setPassword(hash);
      user = quickRepository.createUser(user);
      String jwtToken = Jwts.builder().setSubject("userLogin").claim("user", user)
          .setExpiration(new Date(expire)).setIssuedAt(new Date())
          .signWith(
              SignatureAlgorithm.HS256,
              env.getProperty("secret").getBytes("UTF-8")
          )
          .compact();
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new LoginResponse(jwtToken,expire,HttpStatus.CREATED.value()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  public ResponseEntity login(LoginInput form) {
    try {
      Long expire=System.currentTimeMillis() + 3600000;
      String hash = DigestUtils.sha256Hex(form.getPassword());
      //account already exist return the session token
      User user = quickRepository.getUser(form.getEmail(), hash);
      if (user != null) {
        String jwtToken = Jwts.builder().setSubject("userLogin").claim("user", user)
            .setExpiration(new Date(expire)).setIssuedAt(new Date())
            .signWith(
                SignatureAlgorithm.HS256,
                env.getProperty("secret").getBytes("UTF-8")
            )
            .compact();
        return ResponseEntity.ok(new LoginResponse(jwtToken,expire,HttpStatus.OK.value()));
      }
      //invalid credentials
      else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new BodyMessage("invalid credentials", HttpStatus.UNAUTHORIZED.value()));
      }

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  public ResponseEntity createCart(Cart cart) {
    try {
      if (cart == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new BodyMessage("Cart must not be null", HttpStatus.BAD_REQUEST.value()));
      }

      //validate that products in Cart exist and are in stock
      for (String item : cart.getItems().keySet()) {
        Product product = quickRepository.getProduct(item);
        if (product == null) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
              new BodyMessage("Cart contains invalid product", HttpStatus.BAD_REQUEST.value()));
        } else if (product.getInventoryCount() == 0) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
              new BodyMessage("Cart contains " + product.getTitle() + " which is out of stock",
                  HttpStatus.BAD_REQUEST.value()));
        }
      }

      //calculate Cart total if it is not specified
      cart.setTotalCost(calculateTotal(cart.getItems()));
      return ResponseEntity.status(HttpStatus.CREATED).body(quickRepository.createCart(cart));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  public ResponseEntity getCart() {
    try {
      User user = getUserFromJwt(request);
      //if user is not found then jwt is invalid or user was deleted
      if(user==null){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new BodyMessage("invalid credentials", HttpStatus.UNAUTHORIZED.value()));
      }
      if (user.getCart() == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new BodyMessage("Cart not found", HttpStatus.NOT_FOUND.value()));
      } else {
        return ResponseEntity.ok(user.getCart());
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  public ResponseEntity deleteCart(Long id) {
    try {
      quickRepository.deleteCart(id);
      return ResponseEntity.ok(new BodyMessage("successfully deleted", HttpStatus.OK.value()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  public ResponseEntity addRemoveItemsCart(Map<String, Long> items) {
    try {
      //validate that products to add exist and number
      for (String item : items.keySet()) {
        Product product = quickRepository.getProduct(item);
        if (product == null) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
              new BodyMessage("products to add/remove are invalid",
                  HttpStatus.BAD_REQUEST.value()));
        } else if (product.getInventoryCount() == 0) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
              new BodyMessage(product.getTitle() + " is out of stock",
                  HttpStatus.BAD_REQUEST.value()));
        }
      }
      User user=getUserFromJwt(request);
      //if user is not found then jwt is invalid or user was deleted
      if(user==null){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new BodyMessage("invalid credentials", HttpStatus.UNAUTHORIZED.value()));
      }
      Cart cartToUpdate = user.getCart();
      if (cartToUpdate == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new BodyMessage("Cart not found", HttpStatus.NOT_FOUND.value()));
      }

      //add the value to the Cart items
      for (String item : items.keySet()) {
        //update existing product amount
        if (cartToUpdate.getItems().containsKey(item)) {
          Long count = cartToUpdate.getItems().get(item) + items.get(item);
          //ensure product count is positive or zero at end of change
          if (count >= 0) {
            cartToUpdate.getItems().put(item, count);
            //remove item from Cart when count reaches zero
            if (count == 0) {
              cartToUpdate.getItems().remove(item);
            }
          } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new BodyMessage("cannot remove more product then exist in Cart",
                    HttpStatus.BAD_REQUEST.value()));
          }
        }
        //put in new product
        else {
          //dont add new product if amount to add is zero
          if (items.get(item) != 0) {
            cartToUpdate.getItems().put(item, items.get(item));
          }
        }
      }
      //recalculate Cart total
      cartToUpdate.setTotalCost(calculateTotal(cartToUpdate.getItems()));
      //update Cart
      quickRepository.createCart(cartToUpdate);
      return ResponseEntity.ok(new BodyMessage("items added", HttpStatus.OK.value()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  public ResponseEntity checkOut() {
    try {
      Cart cart = getUserFromJwt(request).getCart();
      List<Product> productList = new ArrayList<Product>();

      if (cart == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new BodyMessage("Cart not found", HttpStatus.NOT_FOUND.value()));
      }

      //check that there are enough products available in inventory before completing checkout
      for (String item : cart.getItems().keySet()) {
        Product product = quickRepository.getProduct(item);
        if (product == null) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
              new BodyMessage(item + "in Cart doesn't exist in store",
                  HttpStatus.BAD_REQUEST.value()));
        } else if (product.getInventoryCount() < cart.getItems().get(item)) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
              new BodyMessage("not enough " + item + " to fulfill request",
                  HttpStatus.BAD_REQUEST.value()));
        } else {
          //put products to be updated in list until all items are checked
          product.setInventoryCount(product.getInventoryCount() - cart.getItems().get(item));
          productList.add(product);
        }
      }
      //update products
      for (Product product : productList) {
        quickRepository.createProduct(product);
      }

      Float total = cart.getTotalCost();
      //clear Cart
      cart.setTotalCost((float) 0);
      cart.setItems(new HashMap<>());
      quickRepository.createCart(cart);

      return ResponseEntity.ok(new CheckOutResponse("checkout completed", total));

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  public ResponseEntity createProduct(Product product) {
    try {
      if (product == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new BodyMessage("product must not be null", HttpStatus.BAD_REQUEST.value()));
      }
      if (quickRepository.getProduct(product.getTitle()) != null) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new BodyMessage("product already exists", HttpStatus.CONFLICT.value()));
      }
      return ResponseEntity.status(HttpStatus.CREATED).body(quickRepository.createProduct(product));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  public ResponseEntity getProduct(String id) {
    try {
      Product product = quickRepository.getProduct(id);
      if (product == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new BodyMessage("product not found", HttpStatus.NOT_FOUND.value()));
      } else {
        return ResponseEntity.ok(product);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  public ResponseEntity deleteProduct(String id) {
    try {
      quickRepository.deleteProduct(id);
      return ResponseEntity.ok(new BodyMessage("successfully deleted", HttpStatus.OK.value()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  public ResponseEntity getAllProducts(Boolean hideOutOfStock) {
    try {
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
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  public ResponseEntity updateProduct(String id, ProductUpdateInput product) {
    try {
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
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new BodyMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
  }

  private Float calculateTotal(Map<String, Long> items) {
    Float total = (float) 0;

    //calculate Cart total
    for (String item : items.keySet()) {
      Float price = quickRepository.getProduct(item).getPrice();
      total += (price * items.get(item));
    }

    return total;
  }

  private User getUserFromJwt(HttpServletRequest request) throws Exception {
    String authHeader = request.getHeader("authorization");
    String token = authHeader.substring(7);
    String secret = Base64.getEncoder().encodeToString(env.getProperty("secret").getBytes());
    String userName = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody()
        .get("user", LinkedHashMap.class).get("user").toString();
    String password = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody()
        .get("user", LinkedHashMap.class).get("password").toString();
    return quickRepository.getUser(userName, password);
  }


}
