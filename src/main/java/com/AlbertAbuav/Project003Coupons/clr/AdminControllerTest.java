package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LogoutDetails;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.security.Information;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.utils.*;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCompanies;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCustomers;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(7)
public class AdminControllerTest implements CommandLineRunner {

    private final ChartUtils chartUtils;
    private final static String B_URL = "http://localhost:8080/admin-service";
    private static LoginDetails loginDetails;
    private static String loggedToken;
    private static HttpHeaders httpHeaders = new HttpHeaders();
    private static HttpEntity<String> entity;

    private final RestTemplate restTemplate;
    private final FactoryUtils factoryUtils;
    private final TokenManager tokenManager;
    private AdminService adminService;

    @Override
    public void run(String... args) {

        Colors.setGreenBoldPrint(ArtUtils.ADMIN_CONTROLLER);

        TestUtils.testAdminInfo("Login to Admin and receive a token");

        loginDetails = new LoginDetails("admin@admin.com", "admin");
        ResponseEntity<String> loggedAdmin = restTemplate.postForEntity(B_URL + "/login", loginDetails, String.class);
        System.out.println("The status code response is: " + loggedAdmin.getStatusCodeValue());
        System.out.println("This is the Token given to the admin: \n" + loggedAdmin.getBody());

        Information information = tokenManager.getMap().get(loggedAdmin.getBody());
        adminService = (AdminService) information.getClientFacade();

        loggedToken = loggedAdmin.getBody();
        httpHeaders.add("Authorization", loggedToken);
        entity = new HttpEntity<>("parameters", httpHeaders);

        TestUtils.testAdminInfo("Add Company");

        Coupon coupon1 = factoryUtils.createCouponOfACompany(11);
        Coupon coupon2 = factoryUtils.createCouponOfACompany(11);
        List<Coupon> coupons1 = new ArrayList<>(Arrays.asList(coupon1, coupon2));
        Company company1 = factoryUtils.createCompany();
        company1.setCoupons(coupons1);

        System.out.println("This is the company to add:");
        chartUtils.printCompany(company1);

        HttpEntity<Company> entity2 = new HttpEntity<>(company1, httpHeaders);
        ResponseEntity<String> addCompany = restTemplate.exchange(B_URL + "/companies", HttpMethod.POST, entity2, String.class);
        System.out.println(addCompany.getStatusCodeValue());

        System.out.println("The company after adding it to the DB:");
        try {
            chartUtils.printCompany(adminService.getSingleCompany(11));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Update Company");

        Company companyToUpdate = null;
        try {
            companyToUpdate = adminService.getSingleCompany(11);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("This is the company before the update:");
        chartUtils.printCompany(companyToUpdate);

        companyToUpdate.setEmail("update@update.com");
        companyToUpdate.setPassword("1234");

        HttpEntity<Company> entity3 = new HttpEntity<>(companyToUpdate, httpHeaders);
        ResponseEntity<String> updateCompany = restTemplate.exchange(B_URL + "/companies", HttpMethod.PUT, entity3, String.class);
        System.out.println(updateCompany.getStatusCodeValue());

        System.out.println("The company after updating it in the DB:");
        Company updated = null;
        try {
            updated = adminService.getSingleCompany(11);
            chartUtils.printCompany(updated);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Delete Company");

        ResponseEntity<String> deleteCompany = restTemplate.exchange(B_URL + "/companies/11", HttpMethod.DELETE, entity, String.class);
        System.out.println(deleteCompany.getStatusCodeValue());

        TestUtils.testAdminInfo("Get all Companies");

        ResponseEntity<ListOfCompanies> getAllCompanies = restTemplate.exchange(B_URL + "/companies", HttpMethod.GET, entity, ListOfCompanies.class);
        System.out.println("The status code response is: " + getAllCompanies.getStatusCodeValue());
        chartUtils.printCompanies(getAllCompanies.getBody().getCompanies());

        TestUtils.testAdminInfo("Get single Company");

        ResponseEntity<Company> getSingleCompany = restTemplate.exchange(B_URL + "/companies/2", HttpMethod.GET, entity, Company.class);
        System.out.println("The status code response is: " + getSingleCompany.getStatusCodeValue());
        chartUtils.printCompany(getSingleCompany.getBody());

        TestUtils.testAdminInfo("Add Customer");

        Customer customer1 = factoryUtils.createCustomer();

        HttpEntity<Customer> entity4 = new HttpEntity<>(customer1, httpHeaders);
        ResponseEntity<String> addCustomer = restTemplate.exchange(B_URL + "/customers", HttpMethod.POST, entity4, String.class);
        System.out.println(addCustomer.getStatusCodeValue());

        Customer addedCustomer = null;
        try {
            addedCustomer = adminService.getSingleCustomer(11);
            System.out.println("This is the added Customer;");
            chartUtils.printCustomer(addedCustomer);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Update Customer");

        Coupon coupon3 = factoryUtils.createCouponOfACompany(8);
        Coupon coupon4 = factoryUtils.createCouponOfACompany(8);
        List<Coupon> companyCoupons1 = new ArrayList<>(Arrays.asList(coupon3, coupon4));

        Company company8 = null;
        try {
            company8 = adminService.getSingleCompany(8);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        company8.setCoupons(companyCoupons1);

        try {
            adminService.updateCompany(company8);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        List<Coupon> customerCoupons1 = null;
        try {
            Company company2 = adminService.getSingleCompany(8);
            customerCoupons1 = new ArrayList<>(Arrays.asList(company2.getCoupons().get(0), company2.getCoupons().get(1)));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        addedCustomer.setCoupons(customerCoupons1);

        HttpEntity<Customer> entity5 = new HttpEntity<>(addedCustomer, httpHeaders);
        ResponseEntity<String> updateCustomer = restTemplate.exchange(B_URL + "/customers", HttpMethod.PUT, entity5, String.class);
        System.out.println(updateCustomer.getStatusCodeValue());

        System.out.println("This is the customer after the update");
        try {
            chartUtils.printCustomer(adminService.getSingleCustomer(11));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Delete Customer");

        ResponseEntity<String> deleteCustomer = restTemplate.exchange(B_URL + "/customers/11", HttpMethod.DELETE, entity, String.class);
        System.out.println(deleteCustomer.getStatusCodeValue());

        TestUtils.testAdminInfo("Get all Customers");

        ResponseEntity<ListOfCustomers> getAllCustomers = restTemplate.exchange(B_URL + "/customers", HttpMethod.GET, entity, ListOfCustomers.class);
        System.out.println("The status code response is: " + getAllCustomers.getStatusCodeValue());
        chartUtils.printCustomers(getAllCustomers.getBody().getCustomers());

        TestUtils.testAdminInfo("Get single Customer");

        ResponseEntity<Customer> getSingleCustomers = restTemplate.exchange(B_URL + "/customers/2", HttpMethod.GET, entity, Customer.class);
        System.out.println("The status code response is: " + getSingleCustomers.getStatusCodeValue());
        chartUtils.printCustomer(getSingleCustomers.getBody());

        TestUtils.testAdminInfo("Logout the Admin");

        LogoutDetails logoutDetails = new LogoutDetails(loggedToken);
        HttpEntity<LogoutDetails> logoutEntity = new HttpEntity<>(logoutDetails, httpHeaders);
        ResponseEntity<String> logoutAdmin = restTemplate.exchange(B_URL + "/logout", HttpMethod.DELETE, logoutEntity , String.class);
        System.out.println("The status code response is: " + logoutAdmin.getStatusCodeValue());

        System.out.println();
        System.out.println();
    }
}
