package com.scm.services;

import java.util.List;
import java.util.Optional;

import com.scm.entities.User;

public interface UserService {

  User saveUser(User user);

   //User getUserById(String id);
  Optional<User> getUserById(String id);

  //User updateUser(User user);
  Optional<User> updateUser(User user);

  void deleteUser(String id); 
  boolean isUserExist(String userId);
  boolean isUserExistByUserName(String email);
  List<User> getAllUsers();

  User getUserByEmail(String email);

  //add more methods




}
