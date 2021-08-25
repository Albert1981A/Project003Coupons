package com.AlbertAbuav.Project003Coupons.controllers.model;

import com.AlbertAbuav.Project003Coupons.login.ClientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDetails {

    private String email;
    private String password;
}
