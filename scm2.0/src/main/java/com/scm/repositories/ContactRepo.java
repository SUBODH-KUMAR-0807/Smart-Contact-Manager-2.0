package com.scm.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;
import com.scm.entities.contact;

@Repository
public interface ContactRepo extends JpaRepository<contact,String>{

  // find the contact by user

  //custom finder method

  // This method find contact page of given user and given page details.
  Page<contact> findByUser(User user, Pageable pageable);

  //custom query methods to get all contacts of a user
  
  @Query("SELECT c FROM contact c WHERE c.user.id= :userId")
  List<contact> findByUserId(@Param("userId") String userId);


  //These method fetch contacts of given User.

  Page<contact> findByUserAndNameContaining(User user, String nameKeyword, Pageable pageable);
  Page<contact> findByUserAndEmailContaining(User user, String emailKeyword, Pageable pageable);
  Page<contact> findByUserAndPhoneNumberContaining(User user, String phoneKeyword, Pageable pageable);

}
