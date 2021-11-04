package com.AlbertAbuav.Project003Coupons.controllers;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginResponse;
import com.AlbertAbuav.Project003Coupons.controllers.model.LogoutDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.RegisterRequest;
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
import com.AlbertAbuav.Project003Coupons.utils.FactoryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
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
    private final FactoryUtils factoryUtils;
    private LoginResponse loginResponse;

    @PostMapping("register")  //==>  http://localhost:8080/client/register
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerNewUser(@RequestBody RegisterRequest registerRequest) throws invalidCompanyException, invalidCustomerException, invalidAdminException {
        if (registerRequest.getClientType().equals(ClientType.COMPANY)) {
            Company company = null;
            if(companyRepository.existsByEmail(registerRequest.getClientEmail())){
                throw new invalidCompanyException("The Company exist!");
            } else {
                try {
                    company = factoryUtils.createCompany();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                company.setName(registerRequest.getClientName());
                company.setEmail(registerRequest.getClientEmail());
                company.setPassword(registerRequest.getClientPassword());
                companyRepository.save(company);
                Company registered = companyRepository.findByEmailAndPassword(company.getEmail(), company.getPassword());
                loginResponse = loginManager.controllerLogin(registered.getEmail(), registered.getPassword(), ClientType.COMPANY);
            }
        } else if (registerRequest.getClientType().equals(ClientType.CUSTOMER)){
            Customer customer = null;
            if(customerRepository.existsByEmail(registerRequest.getClientEmail())){
                throw new invalidCustomerException("The Customer exist!");
            } else {
                try {
                    customer = factoryUtils.createCustomer();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                customer.setFirstName(registerRequest.getClientName());
                customer.setLastName(registerRequest.getClientLastName());
                customer.setEmail(registerRequest.getClientEmail());
                customer.setPassword(registerRequest.getClientPassword());
                customerRepository.save(customer);
                Customer registered2 = customerRepository.findByEmailAndPassword(customer.getEmail(), customer.getPassword());
//                registerRequest.setId(registered2.getId());
                loginResponse = loginManager.controllerLogin(registered2.getEmail(), registered2.getPassword(), ClientType.CUSTOMER);
            }
        }
        return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);  //==> return body + 201
    }

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

    @GetMapping("get-coupons")  //==>  http://localhost:8080/client/get-coupons
    public ResponseEntity<?> getAllCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        return new ResponseEntity<>(coupons, HttpStatus.OK); //==> Return body + 200
    }
}
