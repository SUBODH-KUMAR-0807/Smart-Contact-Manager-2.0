package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

  @NotBlank(message="Username is required")
  @Size(min=3,message="Min 3 characters is required")
  private String name;

  @Email(message="Invalid Email")
  @NotBlank(message="Email is required")
  private String email;

  @NotBlank(message="Phone number is required")
  @Pattern(regexp="^[0-9]{10}$",message="Invalid Phone Number")
  private String phoneNumber;

  @NotBlank(message = "Address is required")
  private String address;

  
  private String description;
  
  private String websiteLink;
  private String linkedInLink;
  private boolean favorite;


  private MultipartFile contactImage;

  private String picture;


}
