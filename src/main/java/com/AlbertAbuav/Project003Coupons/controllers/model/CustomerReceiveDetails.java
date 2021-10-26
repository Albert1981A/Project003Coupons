package com.AlbertAbuav.Project003Coupons.controllers.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(value = {"image"})
public class CustomerReceiveDetails {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String imageID;
    private MultipartFile image;

}
