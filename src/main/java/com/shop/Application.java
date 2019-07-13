package com.shop;

import com.shop.config.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Application {

  @Bean
  public FilterRegistrationBean jwtFilter() {
    final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new JwtFilter());
    registrationBean.addUrlPatterns("/cart/*","/user");

    return registrationBean;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
