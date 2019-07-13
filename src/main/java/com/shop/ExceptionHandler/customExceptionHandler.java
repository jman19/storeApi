package com.shop.ExceptionHandler;

import com.shop.resources.BodyMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice(annotations = RestController.class)
public class customExceptionHandler {
    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity handleMalformedToken(final MalformedJwtException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BodyMessage("JWT Token is Invalid",HttpStatus.UNAUTHORIZED.value()));
    }
    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity handleExpiredToken(final ExpiredJwtException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BodyMessage("JWT Token is Expired",HttpStatus.UNAUTHORIZED.value()));
    }
    @ExceptionHandler({Exception.class})
    public ResponseEntity generalException(final Exception e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BodyMessage("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
