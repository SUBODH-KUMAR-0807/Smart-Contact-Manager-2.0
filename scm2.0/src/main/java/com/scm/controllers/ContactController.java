package com.scm.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.entities.contact;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

  private Logger logger = LoggerFactory.getLogger(ContactController.class);
  

  @Autowired
  private ContactService contactService;

  @Autowired
  private UserService userService;

  @Autowired
  private ImageService imageService;


  //add contact page

  @GetMapping("/add")
  public String addContactView(Model model){

    ContactForm contactForm = new ContactForm();
    model.addAttribute("contactForm",contactForm);
    return "user/add_contact";
  }

  //This method will run when we submit the details from Add Contact(add_contact.html) page.
  @PostMapping("/add")
  public String saveContact(@Valid @ModelAttribute ContactForm contactForm,BindingResult bindingResult, Authentication authentication, HttpSession session ){


    //validate form

    if(bindingResult.hasErrors()){

      session.setAttribute("message",Message.builder().content("Please correct the following errors").type(MessageType.red).build());

      System.out.println(bindingResult.getAllErrors().toString());

      return "user/add_contact";
    }


    // username(email id) store the email.
    String username = Helper.getEmailOfLoggedInUser(authentication);
    User user = userService.getUserByEmail(username);
  

    // form --> contact

    contact contact = new contact();

    contact.setUser(user);
    contact.setName(contactForm.getName());
    contact.setEmail(contactForm.getEmail());
    contact.setPhoneNumber(contactForm.getPhoneNumber());
    contact.setFavorite(contactForm.isFavorite());
    contact.setAddress(contactForm.getAddress());
    contact.setDescription(contactForm.getDescription());
    contact.setLinkedInLink(contactForm.getLinkedInLink());
    contact.setWebsiteLink(contactForm.getWebsiteLink());


    //process or upload image: if image is select in Add Contact form for upload:
    //filename store is id.

    if(!contactForm.getContactImage().isEmpty()){

      String filename = UUID.randomUUID().toString();
      contact.setCloudinaryImagePublicId(filename);
      
      String fileURL = imageService.uploadImage(contactForm.getContactImage(),filename);
      contact.setPicture(fileURL);
      
    }

    logger.info("file information: {}",contactForm.getContactImage().getOriginalFilename());


    //save contact details in database.

    contactService.save(contact);

    System.out.println(contactForm);


    //message

    session.setAttribute("message", Message.builder().content("You have successfully added a new contact").type(MessageType.green).build());

    return "redirect:/user/contacts/add";


  }


  //All contacts
  @GetMapping()
  public String viewContacts(Model model, Authentication authentication,
  
  @RequestParam(value="page", defaultValue="0") int page,

  @RequestParam(value="size", defaultValue = AppConstants.PAGE_SIZE+"") int size,

  @RequestParam(value="sortBy", defaultValue="name") String sortBy,
  @RequestParam(value="direction", defaultValue="asc") String direction
  )

  // sort by and direction work together: This arrange contact lists Alphabetically.
  {

    //load all the user contacts

    // Jisne Login kr rkha hai uski email and name fetch kro.

    String username = Helper.getEmailOfLoggedInUser(authentication);
    User user =  userService.getUserByEmail(username);

    // pageContact store all data of given page.
    Page<contact> pageContact = contactService.getByUser(user,page,size,sortBy,direction);

    model.addAttribute("pageContact",pageContact);

    model.addAttribute("pageSize",AppConstants.PAGE_SIZE);

    model.addAttribute("contactSearchForm", new ContactSearchForm());

    return "user/contacts";
  }


  //search handler

  @RequestMapping("/search")
  public String searchHandler(

    @ModelAttribute ContactSearchForm contactSearchForm,

    //@RequestParam("field") String field,
    //@RequestParam("keyword") String value,

    @RequestParam(value="page", defaultValue="0") int page,
    @RequestParam(value="size", defaultValue = AppConstants.PAGE_SIZE+"") int size,
    @RequestParam(value="sortBy", defaultValue="name") String sortBy,
    @RequestParam(value="direction", defaultValue="asc") String direction,

    Model model, Authentication authentication

  ){
    logger.info("field {} keyword {}",contactSearchForm.getField(),contactSearchForm.getValue());


    var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

    Page<contact> pageContact=null;

    if(contactSearchForm.getField().equalsIgnoreCase("name"))
    {
      pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy,direction, user);
      
    }
    else if(contactSearchForm.getField().equalsIgnoreCase("email")){
      pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy,direction ,user);
    }
    else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
      pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy,direction ,user);
    }

    logger.info("phone no {}",user.getPhoneNumber());
    logger.info("pageContact {}",pageContact);

    // contactSearchForm data pass to attribute.
    model.addAttribute("contactSearchForm",contactSearchForm);

    //pageContact has data of user which was search.
    model.addAttribute("pageContact",pageContact);
    model.addAttribute("pageSize",AppConstants.PAGE_SIZE);
    return "user/search";
  }


  //delete request come from contact.js which have deleteContact(cid) method.

  @RequestMapping("/delete/{contactId}")
  public String deleteContact(@PathVariable("contactId") String contactId,HttpSession session){

    contactService.delete(contactId);

    logger.info("contactId {} deleted",contactId);
    System.out.println("deleted contactId: "+contactId);

    
    session.setAttribute("message", Message.builder()
    .content("Contact is deleted successfully !!")
    .type(MessageType.green)
    .build());
    return "redirect:/user/contacts";

  }

  //update view form: request come from user- contacts.html

  @GetMapping("/view/{contactId}")
  public String updateContactFormView(@PathVariable("contactId") String contactId,Model model){


    //fetch old contact data of user from database and set old details in contactForm, which reflect on update form.

    var contact = contactService.getById(contactId);

    ContactForm contactForm = new ContactForm();
    contactForm.setName(contact.getName());
    contactForm.setEmail(contact.getEmail());
    contactForm.setPhoneNumber(contact.getPhoneNumber());
    contactForm.setAddress(contact.getAddress());
    contactForm.setDescription(contact.getDescription());
    contactForm.setFavorite(contact.isFavorite());
    contactForm.setWebsiteLink(contact.getWebsiteLink());
    contactForm.setLinkedInLink(contact.getLinkedInLink());
    contactForm.setPicture(contact.getPicture());

    model.addAttribute("contactForm",contactForm);
    model.addAttribute("contactId",contactId);

    return "user/update_contact_view";

  }


  @RequestMapping(value="/update/{contactId}",method=RequestMethod.POST)
  public String updateContact(@PathVariable("contactId") String contactId, @Valid
  @ModelAttribute ContactForm contactForm,BindingResult bindingResult ,Model model,HttpSession session){

    if(bindingResult.hasErrors()){
      return "user/update_contact_view";
    }

    //update the contact
    //old data is fetch and new updated values is set.

    var con = contactService.getById(contactId);

    con.setCid(contactId);
    con.setName(contactForm.getName());
    con.setEmail(contactForm.getEmail());
    con.setPhoneNumber(contactForm.getPhoneNumber());
    con.setAddress(contactForm.getAddress());
    con.setDescription(contactForm.getDescription());

    con.setLinkedInLink(contactForm.getLinkedInLink());
    con.setWebsiteLink(contactForm.getWebsiteLink());
    con.setFavorite(contactForm.isFavorite());

    System.out.println("CONTACT FORM PICTURE before process image --------------------"+contactForm.getPicture());

    //process image: if image is select in update form for upload:
    
    if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()){

      logger.info("file is not empty");

      String fileName = UUID.randomUUID().toString();
      con.setCloudinaryImagePublicId(fileName);

      // uploadImage() method give url of image from ImageServiceImpl
      String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);   
      con.setPicture(imageUrl);

      //image url baad me bna hai isliye contactForm me set kra hai.
      contactForm.setPicture(imageUrl);
      
    }

    else{
      logger.info("file is empty");
    }
    

    var updateCon = contactService.update(con);

    logger.info("updated contact {}",updateCon);

    session.setAttribute("message",Message.builder().content("Contact Updated Successfully").type(MessageType.green).build());

    return "redirect:/user/contacts/view/"+contactId;
  }

}
