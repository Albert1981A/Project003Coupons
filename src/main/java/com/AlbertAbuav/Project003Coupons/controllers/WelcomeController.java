package com.AlbertAbuav.Project003Coupons.controllers;

import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.serviceImpl.ClientFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("welcome")  //==>  http://localhost:8080/welcome
public class WelcomeController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ClientFacade clientFacade;

    @PostMapping
    public ResponseEntity<?> register(@RequestParam String name) {
        return new ResponseEntity<>(tokenManager.addToken(clientFacade), HttpStatus.CREATED);
    }
}
