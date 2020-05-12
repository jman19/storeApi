package com.shop.services;
import com.shop.data.QuickRepository;
import com.shop.data.impl.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.LinkedHashMap;

@Service
public class Util {
    @Autowired
    private HttpServletRequest request;
    private QuickRepository quickRepository;
    private Environment env;

    public Util(QuickRepository quickRepository, Environment env) {
        this.quickRepository = quickRepository;
        this.env = env;
    }

    public User getUserFromJwt() throws MalformedJwtException, ExpiredJwtException, SignatureException {
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
