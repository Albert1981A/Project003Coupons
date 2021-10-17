package com.AlbertAbuav.Project003Coupons.controllers.model;


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
public class CompanyResponse {

    private String name;
    private String email;
    private String password;
    private String imageID;
    private MultipartFile image;
}
