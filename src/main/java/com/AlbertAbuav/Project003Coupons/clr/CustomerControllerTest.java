package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LogoutDetails;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.security.Information;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import com.AlbertAbuav.Project003Coupons.utils.*;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCoupons;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCustomers;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(9)
public class CustomerControllerTest implements CommandLineRunner {

    private final ChartUtils chartUtils;
    private final String B_URL = "http://localhost:8080/customer-service";
    private static String loggedToken;
    private static HttpHeaders httpHeaders = new HttpHeaders();
    private static HttpEntity<String> entity;

    private CustomerService customerService;
    private FactoryUtils factoryUtils;
    private CompanyService companyService;
    private final RestTemplate restTemplate;
    private final AdminService adminService;
    private final TokenManager tokenManager;

    @Override
    public void run(String... args) throws Exception {

        Colors.setPurpleBoldPrint(ArtUtils.CUSTOMER_CONTROLLER);

        TestUtils.testCustomerInfo("Login to a Customer and receive a token");

        Customer customerToLogin = adminService.getSingleCustomer(2);
        LoginDetails loginDetails = new LoginDetails(customerToLogin.getEmail(), customerToLogin.getPassword());
        ResponseEntity<String> loggedCustomer = restTemplate.postForEntity(B_URL + "/login", loginDetails, String.class);
        System.out.println("The status code response is: " + loggedCustomer.getStatusCodeValue());
        System.out.println("This is the Token given to the company: \n" + loggedCustomer.getBody());

        Information information = tokenManager.getMap().get(loggedCustomer.getBody());
        customerService = (CustomerService) information.getClientFacade();

        loggedToken = loggedCustomer.getBody();
        httpHeaders.add("Authorization", loggedToken);
        entity = new HttpEntity<>("parameters", httpHeaders);

        TestUtils.testCustomerInfo("Add Coupon");

        // login to company id 2
        Company companyToLogin = adminService.getSingleCompany(2);
        System.out.println("This is the Company to login:");
        LoginDetails loginDetails2 = new LoginDetails(companyToLogin.getEmail(), companyToLogin.getPassword());
        ResponseEntity<String> loggedCompany = restTemplate.postForEntity("http://localhost:8080/company-service/login", loginDetails2, String.class);
        System.out.println("The status code response is: " + loggedCompany.getStatusCodeValue());
        System.out.println("This is the Token given to the company: \n" + loggedCompany.getBody());
        System.out.println();

        Information information2 = tokenManager.getMap().get(loggedCompany.getBody());
        companyService = (CompanyService) information2.getClientFacade();

        Coupon coupon1 = companyService.getSingleCoupon(6);
        System.out.println("this is the coupon to add:");
        chartUtils.printCoupon(coupon1);
        System.out.println();

        HttpEntity<Coupon> entity1 = new HttpEntity<>(coupon1, httpHeaders);
        ResponseEntity<String> addCoupon = restTemplate.exchange(B_URL + "/coupons", HttpMethod.POST, entity1, String.class);
        System.out.println("The status code response is: " + addCoupon.getStatusCodeValue());

        System.out.println("This is the customer after adding the coupon:");
        chartUtils.printCustomer(customerService.getTheLoggedCustomerDetails());

        TestUtils.testCustomerInfo("Get all Customer Coupons");

        ResponseEntity<ListOfCoupons> customerCoupons = restTemplate.exchange(B_URL + "/coupons", HttpMethod.GET, entity, ListOfCoupons.class);
        System.out.println("The status code response is: " + customerCoupons.getStatusCodeValue());
        chartUtils.printCoupons(customerCoupons.getBody().getCoupons());

        TestUtils.testCustomerInfo("Get all Customer Coupons of a specific Category");

        TestUtils.testCustomerInfo("Get all Customer Coupons up to a max price");

        TestUtils.testCustomerInfo("Get the logged Customer details");

        ResponseEntity<Customer> loggedCustomerDetails = restTemplate.exchange(B_URL + "/customer-details", HttpMethod.GET, entity, Customer.class);
        System.out.println("The status code response is: " + loggedCustomerDetails.getStatusCodeValue());
        chartUtils.printCustomer(loggedCustomerDetails.getBody());

        TestUtils.testCustomerInfo("Find all Customers by Coupon id");

//        ResponseEntity<ListOfCustomers> customersByCoupon = restTemplate.exchange(B_URL + "/customers-by-coupon-id/4", HttpMethod.GET, entity, ListOfCustomers.class);
//        System.out.println("The status code response is: " + customersByCoupon.getStatusCodeValue());
//        chartUtils.printCustomers(customersByCoupon.getBody().getCustomers());

        TestUtils.testCustomerInfo("Logout the Customer");

        LogoutDetails logoutDetails = new LogoutDetails(loggedToken);
        HttpEntity<LogoutDetails> logoutEntity = new HttpEntity<>(logoutDetails, httpHeaders);
        ResponseEntity<String> logoutCompany = restTemplate.exchange(B_URL + "/logout", HttpMethod.DELETE, logoutEntity, String.class);
        System.out.println("The status code response is: " + logoutCompany.getStatusCodeValue());

        System.out.println();
        System.out.println();
    }
}
