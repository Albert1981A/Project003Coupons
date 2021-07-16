package com.AlbertAbuav.Project003Coupons.controllers;

import com.AlbertAbuav.Project003Coupons.beans.Category;
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
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer-service")  //==>  http://localhost:8080/customer-service
@RequiredArgsConstructor
public class CustomerController extends ClientController{

    private final CustomerService customerService;
    private final LoginManager loginManager;
    private final TokenManager tokenManager;

    @PostMapping("login") // ==>  http://localhost:8080/customer-service/login
    @Override
    public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails) throws invalidCompanyException, invalidAdminException, invalidCustomerException {
        String token = loginManager.login(loginDetails.getEmail(), loginDetails.getPassword(), ClientType.CUSTOMER);
        return new ResponseEntity<>(token, HttpStatus.CREATED);  //==> return String token + 201
    }

    @DeleteMapping("logout") // ==>  http://localhost:8080/customer-service/logout
    @Override
    public ResponseEntity<?> logOut(@RequestBody LogoutDetails logoutDetails) throws SecurityException {
        if (!tokenManager.isExist(logoutDetails.getToken())) {
            throw new SecurityException("Token doesn't exist in the system !");
        }
        tokenManager.deleteToken(logoutDetails.getToken());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  //==> Return 204 (ok and no content)
    }

    @PostMapping("coupons") // ==>  http://localhost:8080/customer-service/coupons
    public ResponseEntity<?> addCoupon(@RequestHeader(value = "Authorization") String token, @RequestBody Coupon coupon) throws invalidCustomerException, SecurityException {
        if (!tokenManager.isExist(token)) {
            throw new SecurityException("Token doesn't exist in the system !");
        }
        customerService.addCoupon(coupon);
        return new ResponseEntity<>(HttpStatus.CREATED);  //==> Return 201 (created)
    }

    public List<Coupon> getAllCustomerCoupons() throws invalidCustomerException {
        return null;
    }

    public List<Coupon> getAllCustomerCouponsOfSpecificCategory(Category category) throws invalidCustomerException {
        return null;
    }

    public List<Coupon> getAllCustomerCouponsUpToMaxPrice(double maxPrice) throws invalidCustomerException {
        return null;
    }

    public Customer getTheLoggedCustomerDetails() {
        return null;
    }

    public List<Customer> findAllCustomersByCouponId(int couponID) {
        return null;
    }
}
