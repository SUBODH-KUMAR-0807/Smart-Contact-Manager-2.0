package com.scm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class SocialLink {
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  private String link;
  private String title;

  //cid will add in SocialLink table means cid will become foreign key.

  @ManyToOne
  private contact contacts;
}
