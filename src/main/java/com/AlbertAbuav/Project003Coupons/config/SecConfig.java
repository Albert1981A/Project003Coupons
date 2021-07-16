package com.AlbertAbuav.Project003Coupons.config;

import com.AlbertAbuav.Project003Coupons.security.Information;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecConfig {

    @Bean
    public Map<String, Information> map() {
        return new HashMap<>();
    }
}
