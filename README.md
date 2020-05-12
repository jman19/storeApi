# storeApi

This API is the backend for the [Store Front UI](https://github.com/jman19/StoreFront) it is implemented using Spring framework 

# Dependencies
Maven, Java

# How to Run
1) Cd into project folder
2) "mvn spring-boot:run" into terminal

# Swagger UI
once you run succesfully you can access: http://127.0.0.1:8080/swagger-ui.html
This allows you to visualize all API endpoints available 

![Imgur](https://i.imgur.com/0pJmdbW.png)

### JWT Token

Require a JWT token to be included in the Authorization header of the HTTP request related to User

+ GET `/user`
+ GET `/user/orderHistory`
+ GET `/user/cart`
+ PATCH `/user/cart`
+ PATCH `/user/cart/checkout`

If using swagger remember to put the jwts token in this format “Bearer <token here>” into the Authorization field. The space after "Bearer" is necessary.

<img src="https://i.imgur.com/fOAoXCp.png" style="max-width:900px;width:100%">

### Basic auth for store admin account

```
storeAdminUserName: admin
storeAdminPassword: storePass
```

You will need to enter the above login credentials to be authenticated as the store admin to be able to modify the product catalog for your store. Once credentials are entered they are saved in your browser cookie so you don't have to enter them again

+ POST `/employee/product` this endpoint creates a new product for the store
+ PUT `/employee/product/{name}` this endpoint allows one to update a product's details
+ DELETE `/employee/product/{name}` This endpoint deletes a product from store

<img src="https://i.imgur.com/HjYwhV1.png" style="max-width:900px;width:100%">

## Tests

**How to run tests**


```
cd ~/Downloads/<project_folder>
mvn test
```


Integration test our REST API spec and also act as documentation of API requirement. Testing the REST API layer itself and its different endpoint means that our tests capture our requirements and are looking at our implementation as a black box. Thus, they are less likely to break due to some lower level method or database schema changes.

### Current Tests

#### integrationTest()

1. Sign up with email and password
    + `POST /auth/signUp`
        * Expect HttpStatus.CREATED(201) aka our account was created successfully
2. Login with email and password
    + `POST /auth/login`
        * Expect HttpStatus.OK (200) aka our login was successful.
        * Get back JWT token in the response body
    + Each User has a shopping cart associated with account
    
3. Get User Shopping cart
    + `GET /user/cart`
      * Pass in our JWT token in the Authorization header for http request
      * Expect HttpStatus.OK (200) aka our cart was retrieved was successful.
      
4. Add item to shopping cart
    + `PATCH /user/cart` this endpoint modifies the items in a Cart.
        * Pass in our JWT token in the Authorization header for http request
        * In our code we do `user = getUserFromJwt(request);` and get the user and their associated shopping cart.
        
5. Create new product
    + `POST /employee/product` this endpoint adds new items to the shop
        * Requires Basic Auth
        
6. Make purchase with the cart.
    + `PATCH /user/cart/checkout` this endpoint is used for checking out a Cart.
        * Pass in our JWT token in the Authorization header for http request
        * In our code we do `user = getUserFromJwt(request);` and get the user and their associated shopping cart.
7. Check that the inventory count for purchased products.
8. Check that User can change billing information
9. Check that User can change credentials
10. Check that User must enter correct passWord to change credentials

#### cannotPurchaseOutOfStockItems()

1.  Create product "test" with inventory count.
    + `POST /employee/product`
1.  Add product "test" to cart
    + `PATCH /user/cart`
2.  Product "test" goes out of stock while you have it in your cart
    + `PUT /employee/product/test` we set the product inventory to zero
3.  Submit purchase with the cart.
    + `PATCH /user/cart/checkout`
    + Get back HttpStatus.BAD_REQUEST(400) since we cannot purchase out of stock items

#### cannotAddOutOfStockItemsToCart()

1.  Create Product with inventory_count=0
    + `POST /employee/product`
2.  Add to Cart the product with zero inventory.
    + `PATCH /user/cart`
    + Get back HttpStatus.BAD_REQUEST(400) since we cannot add out of stock items to cart.
