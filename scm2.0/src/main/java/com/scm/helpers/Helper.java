package com.scm.helpers;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class Helper {

  public static String getEmailOfLoggedInUser(Authentication authentication){

    
    if(authentication instanceof OAuth2AuthenticationToken){

      var oauth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
      
      var clientId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

      var oauth2User = (DefaultOAuth2User)authentication.getPrincipal();

      String username="";

        //agr login with google kia hai tb : email kese nikale

      if(clientId.equalsIgnoreCase("google"))
      {

        //login with google
        username= oauth2User.getAttribute("email").toString();
        System.out.println("Getting email from google"+username);
        
        
      }

      //agr login with github kia hai tb : email kese nikale
      else if(clientId.equalsIgnoreCase("github")){

          //login with github
          username = oauth2User.getAttribute("email")!=null ?
          oauth2User.getAttribute("email").toString():
          oauth2User.getAttribute("login").toString()+"@gmail.com";

          System.out.println("Getting email from github"+username);

      }

      return username;

    }

  
    // agr email id , password se login kia hai tb : email kese nikale.

    else{

      System.out.println("Getting data from local database");
      return authentication.getName();
    }

    
  }

  //This method is call by UserServiceImpl.java and this link request go to AuthController.

  public static String getLinkForEmailVerification(String emailToken){

    String link ="http://localhost:8080/auth/verify-email?token="+emailToken;

    return link;
  }

}
