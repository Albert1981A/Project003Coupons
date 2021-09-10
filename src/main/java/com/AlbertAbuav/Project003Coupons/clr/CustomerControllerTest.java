package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginResponse;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        ResponseEntity<LoginResponse> loggedCustomer = restTemplate.postForEntity("http://localhost:8080/client/login", loginDetails, LoginResponse.class);
        System.out.println("The status code response is: " + loggedCustomer.getStatusCodeValue());
        System.out.println("This is the Token given to the company: \n" + loggedCustomer.getBody());

        Information information = tokenManager.getMap().get(loggedCustomer.getBody().getClientToken());
        customerService = (CustomerService) information.getClientFacade();

        loggedToken = loggedCustomer.getBody().getClientToken();
        httpHeaders.add("Authorization", loggedToken);
        entity = new HttpEntity<>("parameters", httpHeaders);

        TestUtils.testCustomerInfo("Add Coupon");

        // login to company id 2
        Company companyToLogin = adminService.getSingleCompany(2);
        System.out.println("This is the Company to login:");
        LoginDetails loginDetails2 = new LoginDetails(companyToLogin.getEmail(), companyToLogin.getPassword());
        ResponseEntity<LoginResponse> loggedCompany = restTemplate.postForEntity("http://localhost:8080/client/login", loginDetails2, LoginResponse.class);
        System.out.println("The status code response is: " + loggedCompany.getStatusCodeValue());
        System.out.println("This is the Token given to the company: \n" + loggedCompany.getBody().getClientToken());
        System.out.println();

        Information information2 = tokenManager.getMap().get(loggedCompany.getBody().getClientToken());
        companyService = (CompanyService) information2.getClientFacade();

        Coupon coupon1 = companyService.getSingleCoupon(18);
        System.out.println("this is the coupon to add:");
        chartUtils.printCoupon(coupon1);
        System.out.println();

        HttpEntity<Coupon> entity1 = new HttpEntity<>(coupon1, httpHeaders);
        ResponseEntity<String> addCoupon = restTemplate.exchange(B_URL + "/coupons", HttpMethod.POST, entity1, String.class);
        System.out.println("The status code response is: " + addCoupon.getStatusCodeValue());

        System.out.println("This is the customer after adding the coupon:");
        chartUtils.printCustomer(customerService.getTheLoggedCustomerDetails());

        TestUtils.testCustomerInfo("Get all Customer Coupons");

        ResponseEntity<Coupon[]> customerCoupons = restTemplate.exchange(B_URL + "/coupons", HttpMethod.GET, entity, Coupon[].class);
        System.out.println("The status code response is: " + customerCoupons.getStatusCodeValue());
        List<Coupon> coupons = Arrays.stream(customerCoupons.getBody()).collect(Collectors.toList());
        chartUtils.printCoupons(coupons);

        TestUtils.testCustomerInfo("Get all Customer Coupons of a specific Category");

        System.out.println("The Category to search is: " + coupon1.getCategory());
        ResponseEntity<Coupon[]> categoryCoupons = restTemplate.exchange(B_URL + "/coupons/category/?category=" +coupon1.getCategory(), HttpMethod.GET, entity, Coupon[].class);
        System.out.println("The status code response is: " + categoryCoupons.getStatusCodeValue());
        List<Coupon> coupons2 = Arrays.stream(categoryCoupons.getBody()).collect(Collectors.toList());
        chartUtils.printCoupons(coupons2);

        TestUtils.testCustomerInfo("Get all Customer Coupons up to a max price");

        double max = 78.5;
        System.out.println("The max price to search is: " + max);
        try {
        ResponseEntity<Coupon[]> maxCoupons = restTemplate.exchange(B_URL + "/coupons/max-price/?max-price=" + max, HttpMethod.GET, entity, Coupon[].class);
        System.out.println("The status code response is: " + maxCoupons.getStatusCodeValue());
        List<Coupon> coupons3 = Arrays.stream(maxCoupons.getBody()).collect(Collectors.toList());
        chartUtils.printCoupons(coupons3);
        } catch (IllegalStateException illegalStateException) {
            System.out.println(illegalStateException.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Get the logged Customer details");

        ResponseEntity<Customer> loggedCustomerDetails = restTemplate.exchange(B_URL + "/customer-details", HttpMethod.GET, entity, Customer.class);
        System.out.println("The status code response is: " + loggedCustomerDetails.getStatusCodeValue());
        chartUtils.printCustomer(loggedCustomerDetails.getBody());

        TestUtils.testCustomerInfo("Find all Customers by Coupon id");

        ResponseEntity<Customer[]> customersByCoupon = restTemplate.exchange(B_URL + "/customers-by-coupon-id/4", HttpMethod.GET, entity, Customer[].class);
        System.out.println("The status code response is: " + customersByCoupon.getStatusCodeValue());
        List<Customer> customers = Arrays.stream(customersByCoupon.getBody()).collect(Collectors.toList());
        chartUtils.printCustomers(customers);

        TestUtils.testCustomerInfo("Logout the Customer");

        LogoutDetails logoutDetails = new LogoutDetails(loggedToken);
        HttpEntity<LogoutDetails> logoutEntity = new HttpEntity<>(logoutDetails, httpHeaders);
        ResponseEntity<String> logoutCompany = restTemplate.exchange("http://localhost:8080/client/logout", HttpMethod.DELETE, logoutEntity, String.class);
        System.out.println("The status code response is: " + logoutCompany.getStatusCodeValue());

        System.out.println();
        System.out.println();
    }
}
