package com.scm.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="User")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

//User bhi UserDetails bn jayga, jha pr UserDetails use karna hai vaha User use kr skte hai.
public class User implements UserDetails{

  @Id
  private String userId;

  //name is mandatory
  @Column(name="user_name",nullable=false)
  private String name;

  //email is mandaotry
  @Column(unique=true,nullable = false)
  private String email;

  @Getter(value=AccessLevel.NONE) //it disable the lombok  getter method of password.
  private String password;

  @Column(length=1000)
  private String about;

  private String profilePic;
  private String phoneNumber;

  @Getter(value=AccessLevel.NONE) //it disable the lombok  getter method of enable.
  private boolean enabled = false;

  private boolean emailVerified=false;
  private boolean phoneVerified=false;

  //signup kisse kiye - self, google, facbook, github

  @Enumerated(value = EnumType.STRING) //it is use for save Providers.SELF
  private Providers provider = Providers.SELF;
  private String providerUserId;

  //cid will not add in user table.
  //user id add in contact table

  //cascade is use beacause when we save or delete user then its contacts will automatically save or delete.

  //FetchType.LAZY: query for fetching contact will run when we call or want.

  //Orphan removal means that dependent entities are removed when the relationship to their "parent" entity is destroyed.

  @OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval=true)
  private List<contact> contacts = new ArrayList<>();

  private String emailToken;

  @ElementCollection(fetch=FetchType.EAGER) //we save User in roleList as collection in backend.
  private List<String> roleList = new ArrayList<>();

  //methods of UserDetails


  // getAuthorities() call when we want User ke paas kya role hai.
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    

    // convert list of roles[USER, ADMIN] into collection of SimpleGrantedAuthority[roles{ADMIN, USER}]
    Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
    
      return roles;
  }


  // UserDetails methods:

  // for this project: email hi hmara username hai.

    @Override
    public String getUsername() {
        return this.email;
    }
    
    
    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        
        return true;
    }

    @Override
    public boolean isEnabled() {
        
        return this.enabled;
    }

       
    
}
