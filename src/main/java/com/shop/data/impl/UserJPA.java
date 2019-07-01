package com.shop.data.impl;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJPA extends JpaRepository<User, Long> {

  User findByPasswordAndUser(String password, String user);

  User findByUser(String user);
}
