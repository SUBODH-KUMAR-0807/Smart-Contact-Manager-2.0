package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.User;
import com.scm.entities.contact;


public interface ContactService {

  //save contacts
  contact save(contact contact);

  //update contact
  contact update(contact contact);

  //get contact

  List<contact> getAll();

  //get contact by id

  contact getById(String id);

  //delete contact

  void delete(String id);

  //search contact

  //it return data of user which is search.
  Page<contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order,User user);

  Page<contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order,User user);

  Page<contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order,User user);


  //get contact by userId

  List<contact> getByUserId(String userId);

  //get by user

  Page<contact> getByUser(User user, int page, int size, String sortField, String sortDirection);
}
