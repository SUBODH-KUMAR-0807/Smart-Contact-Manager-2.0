package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

  @Autowired
  private OAuthAuthenticationSuccessHandler handler;


  @Autowired
  private SecurityCustomUserDetailService userDetailService; //it return user data from database when we provide email in their method.

  @Autowired
  private AuthFailureHandler authFailureHandler;


//  configuration of authentication provider for spring security.

  //with the help of methods of DaoAuthenticationProvider we can register our service.

  // DaoAuthenticationProvider retrieves the user’s credentials, such as username and password, from the database and compares them to the credentials provided by the user during login.

  @Bean
  public DaoAuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

    //user details service ka object.
    daoAuthenticationProvider.setUserDetailsService(userDetailService);

    // password encoder ka object.
    // passwordEncoder() value come from BCryptPasswordEncoder().
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

    return daoAuthenticationProvider;
  }

  /*  Password encoder match the password fill by user and
      password which is exist in database of that user.
      BCryptPasswordEncoder(): it convert the password into another form.
  */

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

    //configuration

    // url configure kia hai konse public rhenge aur konse private rhenge.


    /*  
    Login page pr username and password shi fill krne ke baad user se start hone vale sare url public rhenge,
    if we fill wrong username and password then we cannot access page which start from user/**,

    jo url user se start nhi hai ve sare public rhenge.

     */

    httpSecurity.authorizeHttpRequests(authorize->{

      //authorize.requestMatchers("/home","/register","/services").permitAll();

      authorize.requestMatchers("/user/**").authenticated();

      //it enable all request.
      authorize.anyRequest().permitAll();

    });

    //form default login - we manually fill username and password.
    
    // spring security ka login page remove ho jayga and hmara login page show hoga.
    //hmne yha btaya hai ki login page /login pr hai.

    httpSecurity.formLogin(formLogin->{

      formLogin.loginPage("/login");

      // login page pr username vali field ka name email hoga.
      // login page password vali field ka name password hoga.

      formLogin.usernameParameter("email");
      formLogin.passwordParameter("password");

      // login url fire krne pr /home pr phuuch jaynge.
      //formLogin.defaultSuccessUrl("/home");

      //page /login url pr bna hoga and is /authenticate pr processing hogi.
      // (/authentication) bhi "/user/profile" page ko hi show krega or load krega.

      formLogin.loginProcessingUrl("/authenticate");

      //jb form without google or without github successively submit hoga tb hm /user/profile pr forward ho jaynge.
      formLogin.successForwardUrl("/user/profile");

      //jb form submit na ho tb login?error=true url chlega by default.
      formLogin.failureForwardUrl("/login?error=true");

      //when authentication will fail due to enable property is false or other reason then how handle it.

      formLogin.failureHandler(authFailureHandler);
      
    });

    // do-logout url chlane ke liye csrf ko disable krna hoga.

    // spring security ka log out url - http://localhost:8080/logout

    // spring security ka logout url remove ho jayga aur ab '/do-logout' se logout hoga.
    // New logout url - http://localhost:8080/do-logout
    
    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    httpSecurity.logout(logoutForm->{
      logoutForm.logoutUrl("/do-logout");

      //jb successfully logout ho jayga tb login page pr vapis chle jaynge.
      logoutForm.logoutSuccessUrl("/login?logout=true");
    });


    // Login with google and github configuration.

     //oauth configuration

     // if we write this line then we get OAuth 2.0 login page
     // which have two button for login - google and github

     //httpSecurity.oauth2Login(Customizer.withDefaults());

     // we have our login page and we managed login with google and github button on our page.
     // This new code will remove OAuth 2.0 login page.
     //login page pr Login with google and Github ki processing hogi isliye /login likha hai.
     
     //Login with google link - '@{/oauth2/authorization/google}'
     //Login with github link - '@{/oauth2/authorization/github}'
 
    

    httpSecurity.oauth2Login(oauth-> {
      oauth.loginPage("/login");
      oauth.successHandler(handler) ;

      //handler give path /user/profile after successfuly login.

    });

  
    return httpSecurity.build();
    //build return DefaultSecurityFilterChain which is same as SecurityFilterChain
  }


}


//<a href="/oauth2/authorization/google">google</a>
//<a href="/oauth2/authorization/github">github</a>