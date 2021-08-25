package com.AlbertAbuav.Project003Coupons.controllers.model;

import com.AlbertAbuav.Project003Coupons.login.ClientType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {

    private int clientId;
    private String clientToken;
    private String clientName;
    private String clientEmail;

    @Enumerated(EnumType.STRING)
    private ClientType clientType;

}
