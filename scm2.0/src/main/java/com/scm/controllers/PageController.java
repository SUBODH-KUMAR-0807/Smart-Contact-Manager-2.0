package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class PageController {

  @Autowired
  private UserService userService;

  
  @GetMapping("/")
  public String index(){
    return "redirect:/home";
  }

  @GetMapping("/home")
  public String home(Model m){
    return "home";
  }

  //about handler

  @GetMapping("/about")
  public String getMethodName() {
    System.out.println("about handler running...");
      return "about";
  }

  //services

  @GetMapping("/services")
  public String serviceshandler() {
    System.out.println("services page loading");
    return "services";
  }

  @GetMapping("/contact")
  public String contacthandler() {
    System.out.println("contact page loading");
    return "contact";
  }

   // navbar.html have /login url for generate login page.
  @GetMapping("/login")
  public String loginhandler() {
    System.out.println("login page loading");
    return "login";
  }


  // navbar.html have /register url for generate signup page.
  @GetMapping("/register")
  public String registerhandler(Model model) {
    System.out.println("register page loading");

    //when we come second time on this page after submit details then 
    //old details will removed because new userform object pass.

    UserForm userForm = new UserForm();

    model.addAttribute("userForm", userForm);
    return "register";
  }

  //process register or signup

   @RequestMapping( value = "/do-register", method = RequestMethod.POST)
   public String processRegister(@Valid @ModelAttribute UserForm userForm,BindingResult bindingResult ,HttpSession session){

    // @Valid pass data to BindingResult.

    System.out.println("process register");
    

    System.out.println(userForm);

    //validate form data

    if(bindingResult.hasErrors()){
      return "register";
    }


    //validate form data
    //save to database

    //fetch data from userForm and save to user
    User user = new User();
    user.setName(userForm.getName());
    user.setEmail(userForm.getEmail());
    user.setPassword(userForm.getPassword());
    user.setAbout(userForm.getAbout());
    user.setPhoneNumber(userForm.getPhoneNumber());
    user.setEnabled(false);

    user.setProfilePic(
            "https://static-00.iconduck.com/assets.00/profile-default-icon-2048x2045-u3j7s5nj.png");
      
    //save user details in database.
  
    User saveUser = userService.saveUser(user);
    System.out.println("user saved");

      
    //message = "Registration successful"

    Message messagevalue = Message.builder().content("Registration Successful").type(MessageType.green).build();
    session.setAttribute("message",messagevalue);

    //redirect login page

    return "redirect:/register";
  }

  

}
