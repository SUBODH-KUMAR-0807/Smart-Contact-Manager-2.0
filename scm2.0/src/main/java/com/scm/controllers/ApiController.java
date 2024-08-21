package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.entities.contact;
import com.scm.services.ContactService;

@RestController //means hm json ko return krenge.
@RequestMapping("/api")
public class ApiController {

  @Autowired
  private ContactService contactService;



  // get contact

// This mapping is call by loadContactData function from contacts.js.

  @GetMapping("/contacts/{contactId}")
  public contact getContact(@PathVariable String contactId){

    return contactService.getById(contactId);
  }



}
