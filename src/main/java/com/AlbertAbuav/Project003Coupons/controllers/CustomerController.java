package com.AlbertAbuav.Project003Coupons.controllers;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LogoutDetails;
import com.AlbertAbuav.Project003Coupons.exception.SecurityException;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.login.ClientType;
import com.AlbertAbuav.Project003Coupons.login.LoginManager;
import com.AlbertAbuav.Project003Coupons.security.Information;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customer-service")  //==>  http://localhost:8080/customer-service
@RequiredArgsConstructor
public class CustomerController extends ClientController{

    private CustomerService customerService;
    private final LoginManager loginManager;
    private final TokenManager tokenManager;

    @PostMapping("login") // ==>  http://localhost:8080/customer-service/login
    @Override
    public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails) throws invalidCompanyException, invalidAdminException, invalidCustomerException {
        String token = loginManager.login(loginDetails.getEmail(), loginDetails.getPassword(), ClientType.CUSTOMER);
        Information information = tokenManager.getMap().get(token);
        customerService = (CustomerService) information.getClientFacade();
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

    @GetMapping("coupons")  //==>  http://localhost:8080/customer-service/coupons
    public ResponseEntity<?> getAllCustomerCoupons(@RequestHeader(value = "Authorization") String token) throws invalidCustomerException, SecurityException {
        if (!tokenManager.isExist(token)) {
            throw new SecurityException("Token doesn't exist in the system !");
        }
        return new ResponseEntity<>(customerService.getAllCustomerCoupons(), HttpStatus.OK); //==> Return body + 200
    }

    @GetMapping("coupons/by-category")  //==>  http://localhost:8080/customer-service/coupons/by-category
    public ResponseEntity<?> getAllCustomerCouponsOfSpecificCategory(@RequestHeader(value = "Authorization") String token, @RequestParam("category") Category category) throws invalidCustomerException, SecurityException {
        if (!tokenManager.isExist(token)) {
            throw new SecurityException("Token doesn't exist in the system !");
        }
        return new ResponseEntity<>(customerService.getAllCustomerCouponsOfSpecificCategory(category), HttpStatus.OK); //==> Return body + 200
    }

    @GetMapping("coupons/max-price")  //==>  http://localhost:8080/customer-service/coupons/max-price
    public ResponseEntity<?> getAllCustomerCouponsUpToMaxPrice(@RequestHeader(value = "Authorization") String token, @RequestParam double maxPrice) throws invalidCustomerException, SecurityException {
        if (!tokenManager.isExist(token)) {
            throw new SecurityException("Token doesn't exist in the system !");
        }
        return new ResponseEntity<>(customerService.getAllCustomerCouponsUpToMaxPrice(maxPrice), HttpStatus.OK); //==> Return body + 200
    }

    @GetMapping("customer-details")  //==>  http://localhost:8080/customer-service/customer-details
    public ResponseEntity<?>  getTheLoggedCustomerDetails(@RequestHeader(value = "Authorization") String token) throws SecurityException {
        if (!tokenManager.isExist(token)) {
            throw new SecurityException("Token doesn't exist in the system !");
        }
        return new ResponseEntity<>(customerService.getTheLoggedCustomerDetails(), HttpStatus.OK); //==> Return body + 200
    }

    @GetMapping("customers-by-coupon-id/{id}")  //==>  http://localhost:8080/customer-service/customers-by-coupon-id/1 (id=1)
    public ResponseEntity<?>  findAllCustomersByCouponId(@RequestHeader(value = "Authorization") String token, @PathVariable int id) throws SecurityException {
        if (!tokenManager.isExist(token)) {
            throw new SecurityException("Token doesn't exist in the system !");
        }
        return new ResponseEntity<>(customerService.findAllCustomersByCouponId(id), HttpStatus.OK); //==> Return body + 200
    }
}
