package com.AlbertAbuav.Project003Coupons.controllers.model;

import com.AlbertAbuav.Project003Coupons.login.ClientType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private int id;
    private String clientName;
    private String clientLastName;
    private String clientEmail;
    private String clientPassword;

//    @JsonProperty(value = "type")
    @Enumerated(EnumType.STRING)
    private ClientType clientType;
}
