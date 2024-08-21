package com.scm.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class contact {

  @Id
  private String cid;
  private String name;
  private String email;
  private String phoneNumber;
  private String address;
  private String picture;

  @Column(length = 1000)
  private String description;

  private boolean favorite = false;
  private String linkedInLink;
  private String websiteLink;
  private String cloudinaryImagePublicId;

  //userid will add in contact table means it become foreign key, cid will not add in user table.
  
  @ManyToOne
  @JsonIgnore  // it stop - go in contact and go in user and infinite loop.
  private User user;


  //Eager : when query for fetching the user is run then query for fetch SocialLink will run.
  //cid will not add in contact table.

  @OneToMany(mappedBy="contacts",cascade = CascadeType.ALL, fetch=FetchType.EAGER)
  private List<SocialLink> links = new ArrayList<>();


}
