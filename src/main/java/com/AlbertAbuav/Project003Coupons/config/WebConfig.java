package com.AlbertAbuav.Project003Coupons.config;

import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
