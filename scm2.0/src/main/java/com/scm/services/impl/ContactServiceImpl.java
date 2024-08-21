package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.entities.contact;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

  @Autowired
  private ContactRepo contactRepo;

  @Override
  public contact save(contact contact) {
    
    String contactId = UUID.randomUUID().toString();
    contact.setCid(contactId);
    return contactRepo.save(contact);
  }

  @Override
  public contact update(contact contact) {
    
    // new updated data come in contact from con var which use in ContactController updateContact method.
    // contactOld variable store old user data from database.

    var contactOld = contactRepo.findById(contact.getCid()).
    orElseThrow(()->new ResourceNotFoundException("Contact not found"));

    contactOld.setName(contact.getName());
    contactOld.setEmail(contact.getEmail());
    contactOld.setPhoneNumber(contact.getPhoneNumber());

    contactOld.setAddress(contact.getAddress());
    contactOld.setDescription(contact.getDescription());
    contactOld.setWebsiteLink(contact.getWebsiteLink());
    contactOld.setLinkedInLink(contact.getLinkedInLink());

    contactOld.setPicture(contact.getPicture());
    contactOld.setFavorite(contact.isFavorite());
    contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
    

    return contactRepo.save(contactOld);

  }

  @Override
  public List<contact> getAll() {
    return contactRepo.findAll();
  }

  @Override
    public contact getById(String id) {
        return contactRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
    }

  @Override
  public void delete(String id) {

    var contact = contactRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Contact not found with given id " + id));

    contactRepo.delete(contact);
  }

  

  @Override
  public List<contact> getByUserId(String userId) {

    return contactRepo.findByUserId(userId);    

  }

  @Override
  public Page<contact> getByUser(User user, int page, int size, String sortBy, String direction) {

    //: means else

    Sort sort = direction.equals("desc")? 
    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

    // page: page number, size: total pages, sort: Alphabetically sort contact.

    var pageable = PageRequest.of(page,size,sort);
    
    return contactRepo.findByUser(user,pageable);

  }

  @Override
  public Page<contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user) {

    Sort sort = order.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    var pageable = PageRequest.of(page, size, sort);
    
    return contactRepo.findByUserAndNameContaining(user, nameKeyword, pageable);
  }

  @Override
  public Page<contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, User user) {
    
    Sort sort = order.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    var pageable = PageRequest.of(page, size, sort);

    return contactRepo.findByUserAndEmailContaining(user, emailKeyword, pageable);

  }


  @Override
  public Page<contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order, User user) {
    
    Sort sort = order.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    var pageable = PageRequest.of(page, size, sort);

    return contactRepo.findByUserAndPhoneNumberContaining(user,phoneNumberKeyword, pageable);

  }


}
