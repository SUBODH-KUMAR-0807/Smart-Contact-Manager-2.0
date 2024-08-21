package com.scm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.repositories.UserRepo;

@Service
public class SecurityCustomUserDetailService implements UserDetailsService{

  @Autowired
    private UserRepo userRepo;

    // This is UserDetailsService method.

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 

    // it fetch user data  by email from database.

    return userRepo.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found with email: "+username));
    
  }
  // loadUserByUsername take email as username.
  //this class is used for load user by email form database if not found then throw exception

}
