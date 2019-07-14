package com.shop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.shop.data.impl.Product;
import com.shop.resources.CartInput;
import com.shop.resources.LoginInput;
import com.shop.resources.ProductUpdateInput;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", password = "test")
public class ApplicationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Transactional
  public void integrationTest() throws Exception {
    //brand new User
    MvcResult response = mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(new LoginInput("testUser", "password"))))
        .andExpect(status().isCreated())
        .andReturn();

    String jwt = convertJsonStringToMap(response.getResponse().getContentAsString()).get("jwt")
        .toString();

    //existing User
    mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(new LoginInput("testUser", "password"))))
        .andExpect(status().isOk());

    //get an existing Cart
    mockMvc.perform(get("/user/cart").header("Authorization", "Bearer " + jwt))
        .andExpect(status().isOk());

    //create a product
    mockMvc.perform(post("/employee/product").contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(new Product("test", (float) 100, (long) 1000))))
        .andExpect(status().isCreated());

    //add product to existing Cart
    Map<String, Long> items = new HashMap<String, Long>();
    items.put("test", (long) 100);
    mockMvc.perform(patch("/user/cart").header("Authorization", "Bearer " + jwt)
        .contentType(MediaType.APPLICATION_JSON).content(convertToJson(new CartInput(items,false))))
        .andExpect(status().isOk());

    //checkout
    mockMvc.perform(patch("/user/cart/checkout").header("Authorization", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.total").value(10000));

    //check that inventory updated
    mockMvc.perform(get("/shop/product/test"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.inventoryCount").value(900));
  }

  @Test
  @Transactional
  public void cannotPurchaseOutOfStockItems() throws Exception {
    MvcResult response = mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(new LoginInput("testUser", "password"))))
        .andExpect(status().isCreated())
        .andReturn();

    String jwt = convertJsonStringToMap(response.getResponse().getContentAsString()).get("jwt")
        .toString();

    //create a product
    mockMvc.perform(post("/employee/product").contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(new Product("test", (float) 100, (long) 1000))))
        .andExpect(status().isCreated());

    //add product to existing Cart
    Map<String, Long> items = new HashMap<String, Long>();
    items.put("test", (long) 100);
    mockMvc.perform(patch("/user/cart").header("Authorization", "Bearer " + jwt)
        .contentType(MediaType.APPLICATION_JSON).content(convertToJson(new CartInput(items,false))))
        .andExpect(status().isOk());

    //empty product before user can checkout
    mockMvc.perform(put("/employee/product/test").contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(new ProductUpdateInput(0L, 100F))))
        .andExpect(status().isOk());

    //ensure user gets error for trying to checkout without of stock product
    mockMvc.perform(patch("/user/cart/checkout").header("Authorization", "Bearer " + jwt))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void cannotAddOutOfStockItemsToCart() throws Exception {
    MvcResult response = mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(new LoginInput("testUser", "password"))))
        .andExpect(status().isCreated())
        .andReturn();

    String jwt = convertJsonStringToMap(response.getResponse().getContentAsString()).get("jwt")
        .toString();

    //create a product
    mockMvc.perform(post("/employee/product").contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(new Product("test", (float) 100, (long) 0))))
        .andExpect(status().isCreated());

    //add product to existing Cart
    Map<String, Long> items = new HashMap<String, Long>();
    items.put("test", (long) 100);
    mockMvc.perform(patch("/user/cart").header("Authorization", "Bearer " + jwt)
        .contentType(MediaType.APPLICATION_JSON).content(convertToJson(new CartInput(items,false))))
        .andExpect(status().isBadRequest());
  }

  private String convertToJson(Object o) throws Exception {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    return ow.writeValueAsString(o);
  }

  private Map<String, Object> convertJsonStringToMap(String json) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
    });
  }

}
