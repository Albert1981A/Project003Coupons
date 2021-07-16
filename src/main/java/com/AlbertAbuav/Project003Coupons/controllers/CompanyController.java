package com.AlbertAbuav.Project003Coupons.controllers;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LogoutDetails;
import com.AlbertAbuav.Project003Coupons.exception.SecurityException;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.login.ClientType;
import com.AlbertAbuav.Project003Coupons.login.LoginManager;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("company-service")  //==>  http://localhost:8080/company-service
@RequiredArgsConstructor
public class CompanyController extends ClientController{

    private final CompanyService companyService;
    private final LoginManager loginManager;
    private final TokenManager tokenManager;

    @PostMapping("login") // ==>  http://localhost:8080/company-service/login
    @Override
    public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails) throws invalidCompanyException, invalidAdminException, invalidCustomerException {
        String token = loginManager.login(loginDetails.getEmail(), loginDetails.getPassword(), ClientType.COMPANY);
        return new ResponseEntity<>(token, HttpStatus.CREATED);  //==> return String token + 201
    }

    @DeleteMapping("logout") // ==>  http://localhost:8080/company-service/logout
    @Override
    public ResponseEntity<?> logOut(@RequestBody LogoutDetails logoutDetails) throws SecurityException {
        if (!tokenManager.isExist(logoutDetails.getToken())) {
            throw new SecurityException("Token doesn't exist in the system !");
        }
        tokenManager.deleteToken(logoutDetails.getToken());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  //==> Return 204 (ok and no content)
    }

    @PostMapping("coupons") // ==>  http://localhost:8080/company-service/coupons
    public ResponseEntity<?> addCoupon(@RequestHeader(value = "Authorization") String token, @RequestBody Coupon coupon) throws invalidCompanyException, SecurityException {
        if (!tokenManager.isExist(token)) {
            throw new SecurityException("Token doesn't exist in the system !");
        }
        companyService.addCoupon(coupon);
        return new ResponseEntity<>(HttpStatus.CREATED);  //==> Return 201 (created)
    }

    public ResponseEntity<?> updateCoupon(Coupon coupon) throws invalidCompanyException {
        return null;
    }

    public ResponseEntity<?> deleteCoupon(Coupon coupon) throws invalidCompanyException {
        return null;
    }

    public List<Coupon> getAllCompanyCoupons() {
        return null;
    }

    public List<Coupon> getAllCompanyCouponsOfSpecificCategory(Category category) throws invalidCompanyException {
        return null;
    }

    public List<Coupon> getAllCompanyCouponsUpToMaxPrice(double maxPrice) throws invalidCompanyException {
        return null;
    }

    public Company getTheLoggedCompanyDetails() {
        return null;
    }

    public Coupon getSingleCoupon(int id) throws invalidCompanyException {
        return null;
    }

    public List<Customer> getAllCompanyCustomersOfASingleCouponByCouponId(int couponID) throws invalidCompanyException {
        return null;
    }

}
