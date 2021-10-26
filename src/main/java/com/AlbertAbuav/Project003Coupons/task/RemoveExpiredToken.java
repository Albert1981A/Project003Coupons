package com.AlbertAbuav.Project003Coupons.task;

import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveExpiredToken {

    private final TokenManager tokenManager;

    @Scheduled(fixedRate = 1000*60*10)
    public void deleteEvery10Minutes() {
        System.out.println("Delete expired tokens in action!");
        tokenManager.removeExpired();
    }
}
