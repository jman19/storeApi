package com.shop.services;

import com.shop.data.QuickRepository;
import com.shop.data.impl.Cart;
import com.shop.data.impl.User;
import com.shop.resources.BodyMessage;
import com.shop.resources.LoginInput;
import com.shop.resources.LoginResponse;
import com.shop.resources.SignUpInput;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServices {
    private QuickRepository quickRepository;
    private Environment env;

    public AuthServices(QuickRepository quickRepository, Environment env) {
        this.quickRepository = quickRepository;
        this.env = env;
    }

    public ResponseEntity createAccount(SignUpInput form) throws Exception{
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
        //set user billing info
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setCity(form.getCity());
        user.setBillingAddress(form.getBillingAddress());
        user.setProvince(form.getProvince());
        user.setPostalCode(form.getPostalCode());
        user.setPhone(form.getPhone());

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
    }

    public ResponseEntity login(LoginInput form) throws Exception{
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
    }
}
