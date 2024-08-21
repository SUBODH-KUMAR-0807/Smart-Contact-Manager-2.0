package com.scm.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.scm.helpers.Message;
import com.scm.helpers.MessageType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthFailureHandler implements AuthenticationFailureHandler{

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    
        // if we fill right username and password but user is disabled then we again back to login page with message.

        //means exception DisabledException ka object hai.
        if(exception instanceof DisabledException){

          //user is disabled

          HttpSession session =request.getSession();

          session.setAttribute("message", Message.builder().content("User is disabled, Email with verification link is send on your email id !!").type(MessageType.red).build());

          response.sendRedirect("/login");

        }
        else{
            //if exception is not instance of DisableException.
            // if we fill wrong username or password.

            response.sendRedirect("/login?error=true");
        }


  }

}
