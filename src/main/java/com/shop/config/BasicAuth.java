package com.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class BasicAuth extends WebSecurityConfigurerAdapter {

  @Autowired
  private Environment env;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .mvcMatchers(HttpMethod.POST, "/product").authenticated()
        .mvcMatchers(HttpMethod.PUT, "/product/*").authenticated()
        .mvcMatchers(HttpMethod.DELETE, "/product/*").authenticated()
        .anyRequest().permitAll()
        .and()
        .httpBasic()
        .and()
        .csrf().disable();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser(env.getProperty("storeAdminUserName"))
        .password(passwordEncoder().encode(env.getProperty("storeAdminPassword"))).roles("Admin");
  }

}
