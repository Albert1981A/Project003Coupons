package com.AlbertAbuav.Project003Coupons.controllers;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.beans.Image;
import com.AlbertAbuav.Project003Coupons.controllers.model.CompanyResponse;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginResponse;
import com.AlbertAbuav.Project003Coupons.controllers.model.LogoutDetails;
import com.AlbertAbuav.Project003Coupons.exception.*;
import com.AlbertAbuav.Project003Coupons.exception.SecurityException;
import com.AlbertAbuav.Project003Coupons.login.ClientType;
import com.AlbertAbuav.Project003Coupons.login.LoginManager;
import com.AlbertAbuav.Project003Coupons.repositories.CompanyRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CouponRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CustomerRepository;
import com.AlbertAbuav.Project003Coupons.security.Information;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import com.AlbertAbuav.Project003Coupons.service.ImageService;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCompanies;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCustomers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("admin-service")  //==>  http://localhost:8080/admin-service
@RequiredArgsConstructor
public class AdminController {

    private AdminService adminService;
    private final TokenManager tokenManager;
    private final ImageService imageService;

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

//    @ResponseStatus(code= HttpStatus.CREATED)
//    @PostMapping("companies") // ==>  http://localhost:8080/admin-service/companies
//    public ResponseEntity<?> addCompany(@RequestHeader(value = "Authorization") String token, @RequestBody Company company) throws invalidAdminException {
//        tokenManager.updateToken(token); //update the token time to current time
//        adminService = getAdminService(token);
//        System.out.println(company);
//        adminService.addCompany(company);
//        Company added = adminService.getSingleCompany(company.getId());
//        return new ResponseEntity<>(added, HttpStatus.CREATED);  //==> Return 201 (created)
//    }

    @ResponseStatus(code= HttpStatus.CREATED)
    @PostMapping("companies") // ==>  http://localhost:8080/admin-service/companies
    public ResponseEntity<?> addCompany(@RequestHeader(value = "Authorization") String token, @ModelAttribute CompanyResponse companyResponse) throws invalidAdminException {
        System.out.println("Hi... Im here-1 !");
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        System.out.println("Hi... Im here-2 !");
        UUID uuid = null;
        Image image = null;
        try {
            System.out.println("Hi... Im here-3 !");
            System.out.println(companyResponse);
            System.out.println(companyResponse.getImage().getBytes());
            uuid = imageService.addImage(companyResponse.getImage().getBytes());

            image = imageService.getImage(uuid);

        } catch (invalidImageException | IOException e) {
            System.out.println("Hi... Im here-4 !");
            System.out.println(e.getMessage());
        }
        Company company = Company.builder()
                .name(companyResponse.getName())
                .email(companyResponse.getEmail())
                .password(companyResponse.getPassword())
                .imageID(uuid.toString())
                .image(image)
                .build();
        System.out.println("Company to add: " + company);
        adminService.addCompany(company);
        Company added = adminService.getSingleCompany(company.getId());
        return new ResponseEntity<>(added, HttpStatus.CREATED);  //==> Return 201 (created)
    }

    @ResponseStatus(code= HttpStatus.CREATED)
    @PutMapping("companies") // ==>  http://localhost:8080/admin-service/companies
    public ResponseEntity<?> updateCompany(@RequestHeader(value = "Authorization") String token, @RequestBody Company company) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        adminService.updateCompany(company);
        Company updated = adminService.getSingleCompany(company.getId());
        return new ResponseEntity<>(updated, HttpStatus.CREATED); //==> Return 201 (created)
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

    @ResponseStatus(code= HttpStatus.CREATED)
    @PostMapping("customers") // ==>  http://localhost:8080/admin-service/customers
    public ResponseEntity<?> addCustomer(@RequestHeader(value = "Authorization") String token, @RequestBody Customer customer) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        adminService.addCustomer(customer);
        Customer added = adminService.getSingleCustomer(customer.getId());
        return new ResponseEntity<>(added, HttpStatus.CREATED);  //==> Return 201 (created)
    }

    @ResponseStatus(code= HttpStatus.CREATED)
    @PutMapping("customers") // ==>  http://localhost:8080/admin-service/customers
    public ResponseEntity<?> updateCustomer(@RequestHeader(value = "Authorization") String token, @RequestBody Customer customer) throws invalidAdminException {
        tokenManager.updateToken(token); //update the token time to current time
        adminService = getAdminService(token);
        adminService.updateCustomer(customer);
        Customer updated = adminService.getSingleCustomer(customer.getId());
        return new ResponseEntity<>(updated, HttpStatus.CREATED); //==> Return 201 (created)
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
