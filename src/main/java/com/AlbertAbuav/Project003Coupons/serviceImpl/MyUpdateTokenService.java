package com.AlbertAbuav.Project003Coupons.serviceImpl;

import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUpdateTokenService {

    private final TokenManager tokenManager;

    public void updateToken(String token) {
        System.out.println("Update action of token: " + token);
        tokenManager.updateToken(token);
    }
}
