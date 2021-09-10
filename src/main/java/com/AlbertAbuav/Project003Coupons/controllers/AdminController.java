package com.AlbertAbuav.Project003Coupons.controllers;

import com.AlbertAbuav.Project003Coupons.beans.Company;
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
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCompanies;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCustomers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin-service")  //==>  http://localhost:8080/admin-service
@RequiredArgsConstructor
public class AdminController {

    private AdminService adminService;
    private final TokenManager tokenManager;

//    @PostMapping("login") // ==>  http://localhost:8080/admin-service/login
//    @Override
//    public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails) throws invalidCompanyException, invalidAdminException, invalidCustomerException {
//        String token = loginManager.login(loginDetails.getEmail(), loginDetails.getPassword(), ClientType.ADMINISTRATOR);
//        Information information = tokenManager.getMap().get(token);
//        adminService = (AdminService) information.getClientFacade();
//        return new ResponseEntity<>(token, HttpStatus.CREATED);  //==> return String token + 201
//    }

//    @DeleteMapping("logout") // ==>  http://localhost:8080/admin-service/logout
//    @Override
//    public ResponseEntity<?> logOut(@RequestHeader(value = "Authorization") String token, @RequestBody LogoutDetails logoutDetails) {
//        tokenManager.deleteToken(logoutDetails.getToken());
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  //==> Return 204 (ok and no content)
//    }

    public AdminService getAdminService(String token) {
        Information information = tokenManager.getMap().get(token);
        return (AdminService) information.getClientFacade();
    }

    @PostMapping("companies") // ==>  http://localhost:8080/admin-service/companies
    public ResponseEntity<?> addCompany(@RequestHeader(value = "Authorization") String token, @RequestBody Company company) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        System.out.println(company);
        adminService.addCompany(company);
        return new ResponseEntity<>(HttpStatus.CREATED);  //==> Return 201 (created)
    }

    @PutMapping("companies") // ==>  http://localhost:8080/admin-service/companies
    public ResponseEntity<?> updateCompany(@RequestHeader(value = "Authorization") String token, @RequestBody Company company) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        adminService.updateCompany(company);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //==> Return 204 (ok and no content)
    }

    @DeleteMapping("companies/{id}")  // ==>  http://localhost:8080/admin-service/companies/1  (id=1)
    public ResponseEntity<?> deleteCompany(@RequestHeader(value = "Authorization") String token, @PathVariable int id) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        Company companyToDelete = adminService.getSingleCompany(id);
        adminService.deleteCompany(companyToDelete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //==> Return 204 (ok and no content)
    }

    @GetMapping("companies")  //==>  http://localhost:8080/admin-service/companies
    public ResponseEntity<?> getAllCompanies(@RequestHeader(value = "Authorization") String token) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        //return new ResponseEntity<List<Company>>(adminService.getAllCompanies(), HttpStatus.OK); //==> Return body + 200
        return new ResponseEntity<>(adminService.getAllCompanies(), HttpStatus.OK); //==> Return body + 200
    }

    @GetMapping("companies/{id}")  // ==>  http://localhost:8080/admin-service/companies/1  (id=1)
    public ResponseEntity<?> getSingleCompany(@RequestHeader(value = "Authorization") String token, @PathVariable int id) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        return new ResponseEntity<>(adminService.getSingleCompany(id), HttpStatus.OK); //==> Return body + 200
    }

    @PostMapping("customers") // ==>  http://localhost:8080/admin-service/customers
    public ResponseEntity<?> addCustomer(@RequestHeader(value = "Authorization") String token, @RequestBody Customer customer) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        adminService.addCustomer(customer);
        return new ResponseEntity<>(HttpStatus.CREATED);  //==> Return 201 (created)
    }

    @PutMapping("customers") // ==>  http://localhost:8080/admin-service/customers
    public ResponseEntity<?> updateCustomer(@RequestHeader(value = "Authorization") String token, @RequestBody Customer customer) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        adminService.updateCustomer(customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //==> Return 204 (ok and no content)
    }

    @DeleteMapping("customers/{id}")  // ==>  http://localhost:8080/admin-service/customers/1  (id=1)
    public ResponseEntity<?> deleteCustomer(@RequestHeader(value = "Authorization") String token, @PathVariable int id) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        Customer customerToDelete = adminService.getSingleCustomer(id);
        adminService.deleteCustomer(customerToDelete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //==> Return 204 (ok and no content)
    }

    @GetMapping("customers")  //==>  http://localhost:8080/admin-service/customers
    public ResponseEntity<?> getAllCustomers(@RequestHeader(value = "Authorization") String token) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        //return new ResponseEntity<>(new ListOfCustomers(adminService.getAllCustomers()), HttpStatus.OK); //==> Return body + 200
        return new ResponseEntity<>(adminService.getAllCustomers(), HttpStatus.OK); //==> Return body + 200
    }

    @GetMapping("customers/{id}")  // ==>  http://localhost:8080/admin-service/customers/1  (id=1)
    public ResponseEntity<?> getSingleCustomer(@RequestHeader(value = "Authorization") String token, @PathVariable int id) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        return new ResponseEntity<>(adminService.getSingleCustomer(id), HttpStatus.OK); //==> Return body + 200
    }
}
