package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
//it used for we can use bean of this class.
//This class is used for save data in database after login .


//This class is used when we sign or login with google or github.
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler{


  @Autowired
  private UserRepo userRepo;


  //it used for print data on console

  Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);


  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    
        logger.info("OAuthAuthenticationSuccessHandler");
        
        //OAuth2AuthenticationToken class is used for get Client Registration Id.

        var oauth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;

        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId); //it print google or github on console .

        /* --------------------------------------------------------------------------------- */

        //DefaultOAuth2User and getPrinciple is used for get User details after login by user.

        var oauthUser = (DefaultOAuth2User)authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key,value)->{
          logger.info(key+" : "+value);
        });


        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setPassword("dummy");



        //google login

        if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
          
          //oauthUser.getAttribute() fetch data from google server

          user.setName(oauthUser.getAttribute("name").toString());
          user.setEmail(oauthUser.getAttribute("email").toString());
          user.setProfilePic(oauthUser.getAttribute("picture").toString());
          user.setProviderUserId(oauthUser.getName());
          user.setProvider(Providers.GOOGLE);
          user.setAbout("This account is created using google");

        }

        // github login
        else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){

          //fetch user details from github server or after login with github

          // "login" give username
          
          String name = oauthUser.getAttribute("login").toString();
          String email = oauthUser.getAttribute("email")!=null ?  
          oauthUser.getAttribute("email").toString() :
          oauthUser.getAttribute("login").toString()+"@gmail.com"; 

          // avatar_url store the picture on github server
          String picture = oauthUser.getAttribute("avatar_url").toString();        
          String providerUserId = oauthUser.getName();


          // set User data in database

          user.setEmail(email);
          user.setName(name);
          user.setProfilePic(picture);
          user.setProviderUserId(providerUserId);
          user.setProvider(Providers.GITHUB);
          user.setAbout("This account is created using github");

        }

        //login with linkedin
        else if(authorizedClientRegistrationId.equalsIgnoreCase("linkedin")){

        }

        else{
          logger.info("OAuthAuthenticationSuccessHandler: Unknown provider");
        }


        //if user is exist in database then we will not save the user, otherwise save the user.

        User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);

        if(user2==null)
        {
          //save user data in database after login.

          userRepo.save(user);
          logger.info("user saved: "+user.getEmail());
        }
        
        
        //it redirect our page.
        //response.sendRedirect("/user/profile");

        // redirect our page.
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    
  }

}
