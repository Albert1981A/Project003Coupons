package com.AlbertAbuav.Project003Coupons.controllers;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LogoutDetails;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.login.ClientType;
import com.AlbertAbuav.Project003Coupons.login.LoginManager;
import com.AlbertAbuav.Project003Coupons.security.Information;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCompanies;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCoupons;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCustomers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("company-service")  //==>  http://localhost:8080/company-service
@RequiredArgsConstructor
public class CompanyController extends ClientController{

    private CompanyService companyService;
    private final LoginManager loginManager;
    private final TokenManager tokenManager;

    @PostMapping("login") // ==>  http://localhost:8080/company-service/login
    @Override
    public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails) throws invalidCompanyException, invalidAdminException, invalidCustomerException {
        String token = loginManager.login(loginDetails.getEmail(), loginDetails.getPassword(), ClientType.COMPANY);
        Information information = tokenManager.getMap().get(token);
        companyService = (CompanyService) information.getClientFacade();
        return new ResponseEntity<>(token, HttpStatus.CREATED);  //==> return String token + 201
    }

    @DeleteMapping("logout") // ==>  http://localhost:8080/company-service/logout
    @Override
    public ResponseEntity<?> logOut(@RequestHeader(value = "Authorization") String token, @RequestBody LogoutDetails logoutDetails) {
        tokenManager.deleteToken(logoutDetails.getToken());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  //==> Return 204 (ok and no content)
    }

    @PostMapping("coupons") // ==>  http://localhost:8080/company-service/coupons
    public ResponseEntity<?> addCoupon(@RequestHeader(value = "Authorization") String token, @RequestBody Coupon coupon) throws invalidCompanyException {
        companyService.addCoupon(coupon);
        return new ResponseEntity<>(HttpStatus.CREATED);  //==> Return 201 (created)
    }

    @PutMapping("coupons") // ==>  http://localhost:8080/company-service/coupons
    public ResponseEntity<?> updateCoupon(@RequestHeader(value = "Authorization") String token, @RequestBody Coupon coupon) throws invalidCompanyException {
        companyService.updateCoupon(coupon);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //==> Return 204 (ok and no content)
    }

    @DeleteMapping("coupons/{id}")  // ==>  http://localhost:8080/company-service/coupons/1  (id=1)
    public ResponseEntity<?> deleteCoupon(@RequestHeader(value = "Authorization") String token, @PathVariable int id) throws invalidCompanyException {
        Coupon couponToDelete = companyService.getSingleCoupon(id);
        companyService.deleteCoupon(couponToDelete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //==> Return 204 (ok and no content)
    }

    @GetMapping("coupons")  //==>  http://localhost:8080/company-service/coupons
    public ResponseEntity<?> getAllCompanyCoupons(@RequestHeader(value = "Authorization") String token) {
        return new ResponseEntity<>(new ListOfCoupons(companyService.getAllCompanyCoupons()), HttpStatus.OK); //==> Return body + 200
    }

    @GetMapping("coupons/category")  //==>  http://localhost:8080/company-service/coupons/category/?category=1  (category=1)
    public ResponseEntity<?> getAllCompanyCouponsOfSpecificCategory(@RequestHeader(value = "Authorization") String token, @RequestParam(value = "category") Category category) throws invalidCompanyException {
        return new ResponseEntity<>(new ListOfCoupons(companyService.getAllCompanyCouponsOfSpecificCategory(category)), HttpStatus.OK); //==> Return body + 200
    }

    @GetMapping("coupons/max-price")  //==>  http://localhost:8080/company-service/coupons/max-price/?max-price=80.2  (max-price=80.2)
    public ResponseEntity<?> getAllCompanyCouponsUpToMaxPrice(@RequestHeader(value = "Authorization") String token, @RequestParam(value = "max-price") double maxPrice) throws invalidCompanyException {
        return new ResponseEntity<>(new ListOfCoupons(companyService.getAllCompanyCouponsUpToMaxPrice(maxPrice)), HttpStatus.OK); //==> Return body + 200
    }

    @GetMapping("company-details")  //==>  http://localhost:8080/company-service/company-details
    public ResponseEntity<?> getTheLoggedCompanyDetails(@RequestHeader(value = "Authorization") String token) {
        return new ResponseEntity<>(companyService.getTheLoggedCompanyDetails(), HttpStatus.OK); //==> Return body + 200
    }

    @GetMapping("coupons/{id}")  //==>  http://localhost:8080/company-service/coupons/1  (id=1)
    public ResponseEntity<?> getSingleCoupon(@RequestHeader(value = "Authorization") String token, @PathVariable int id) throws invalidCompanyException {
        return new ResponseEntity<>(companyService.getSingleCoupon(id), HttpStatus.OK); //==> Return body + 200
    }

    @GetMapping("company-customers-by-coupon-id/{id}")  //==>  http://localhost:8080/company-service/company-customers-by-coupon-id/1 (id=1)
    public ResponseEntity<?> getAllCompanyCustomersOfASingleCouponByCouponId(@RequestHeader(value = "Authorization") String token, @PathVariable int id) throws invalidCompanyException {
        return new ResponseEntity<>(new ListOfCustomers(companyService.getAllCompanyCustomersOfASingleCouponByCouponId(id)), HttpStatus.OK); //==> Return body + 200
    }

}
