package com.shop.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.GenericFilterBean;

public class JwtFilter extends GenericFilterBean {

  @Autowired
  private Environment env;

  public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
      throws IOException, ServletException {

    final HttpServletRequest request = (HttpServletRequest) req;
    final HttpServletResponse response = (HttpServletResponse) res;
    final String authHeader = request.getHeader("authorization");

    if ("OPTIONS".equals(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);

      chain.doFilter(req, res);
    } else {
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        throw new ServletException("Missing or invalid Authorization header");
      }

      chain.doFilter(req, res);
    }
  }
}
