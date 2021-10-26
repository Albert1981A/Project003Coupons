package com.AlbertAbuav.Project003Coupons.aop;

import com.AlbertAbuav.Project003Coupons.serviceImpl.MyUpdateTokenService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class MyAspect {

    private final MyUpdateTokenService myUpdateTokenService;

//    @Before("execution(* com.AlbertAbuav.Project003Coupons.controllers.*.*(..)) && args(token,..)")
    @Before("@annotation(MyUpdateToken) && args(token,..)")
    public void updateTokenAdvice(String token) {
        if (!token.equals("undefine")){
            myUpdateTokenService.updateToken(token);
        }
    }
}
