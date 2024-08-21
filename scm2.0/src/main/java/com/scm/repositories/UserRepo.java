package com.scm.repositories;

import java.util.Optional;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User,String> {

  //spring jpa itself create query for these method

  Optional<User> findByEmail(String email);
  Optional<User> findByEmailAndPassword(String email, String password);

  Optional<User> findByEmailToken(String id);

  public void save(SecurityProperties.User user1);

}
