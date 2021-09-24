package com.AlbertAbuav.Project003Coupons.controllers;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginResponse;
import com.AlbertAbuav.Project003Coupons.controllers.model.LogoutDetails;
import com.AlbertAbuav.Project003Coupons.exception.SecurityException;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.login.ClientType;
import com.AlbertAbuav.Project003Coupons.login.LoginManager;
import com.AlbertAbuav.Project003Coupons.repositories.CompanyRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CouponRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CustomerRepository;
import com.AlbertAbuav.Project003Coupons.security.Information;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("client")  //==>  http://localhost:8080/client
@RequiredArgsConstructor
public class LogController {

    private final TokenManager tokenManager;
    protected final CouponRepository couponRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final LoginManager loginManager;
    private LoginResponse loginResponse;


    @PostMapping("login") // ==>  http://localhost:8080/client/login
    public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails) throws invalidCompanyException, invalidAdminException, invalidCustomerException, SecurityException {
        if (companyRepository.existsByEmailAndPassword(loginDetails.getEmail(), loginDetails.getPassword())) {
            loginResponse = loginManager.controllerLogin(loginDetails.getEmail(), loginDetails.getPassword(), ClientType.COMPANY);
        } else if (customerRepository.existsByEmailAndPassword(loginDetails.getEmail(), loginDetails.getPassword())) {
            loginResponse = loginManager.controllerLogin(loginDetails.getEmail(), loginDetails.getPassword(), ClientType.CUSTOMER);
        } else if (loginDetails.getEmail().equals("admin@admin.com") && loginDetails.getPassword().equals("admin")) {
            loginResponse = loginManager.controllerLogin(loginDetails.getEmail(), loginDetails.getPassword(), ClientType.ADMINISTRATOR);
        } else {
            throw new SecurityException("Login error...!");
        }
        return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);  //==> return String token + 201
    }

    @DeleteMapping("logout") // ==>  http://localhost:8080/client/logout
    public ResponseEntity<?> logOut(@RequestHeader(value = "Authorization") String token) {
        tokenManager.deleteToken(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  //==> Return 204 (ok and no content)
    }

    @GetMapping("coupons")  //==>  http://localhost:8080/client/coupons
    public ResponseEntity<?> getAllCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        return new ResponseEntity<>(coupons, HttpStatus.OK); //==> Return body + 200
    }
}
