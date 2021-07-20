package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import com.AlbertAbuav.Project003Coupons.utils.ArtUtils;
import com.AlbertAbuav.Project003Coupons.utils.ChartUtils;
import com.AlbertAbuav.Project003Coupons.utils.Colors;
import com.AlbertAbuav.Project003Coupons.utils.TestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(9)
public class CustomerControllerTest implements CommandLineRunner {

    private final ChartUtils chartUtils;
    private final static String B_URL = "http://localhost:8080/customer-service";

    private final RestTemplate restTemplate;
    private final CustomerService customerService;
    private final AdminService adminService;

    @Override
    public void run(String... args) throws Exception {

        Colors.setPurpleBoldPrint(ArtUtils.CUSTOMER_CONTROLLER);

        TestUtils.testCustomerInfo("Login to a Customer and receive a token");

        Customer customerToLogin = adminService.getSingleCustomer(2);
        LoginDetails loginDetails = new LoginDetails(customerToLogin.getEmail(), customerToLogin.getPassword());
        ResponseEntity<String> loggedCustomer = restTemplate.postForEntity(B_URL + "/login", loginDetails, String.class);
        System.out.println("The status code response is: " + loggedCustomer.getStatusCodeValue());
        System.out.println("This is the Token given to the company: \n" + loggedCustomer.getBody());
        System.out.println();

        TestUtils.testCustomerInfo("Add Coupon");

        TestUtils.testCustomerInfo("Get all Customer Coupons");

        TestUtils.testCustomerInfo("Get all Customer Coupons of a specific Category");

        TestUtils.testCustomerInfo("Get all Customer Coupons up to a max price");

        TestUtils.testCustomerInfo("Get the logged Customer details");

        TestUtils.testCustomerInfo("Find all Customers by Coupon id");

        TestUtils.testCustomerInfo("Logout the Customer");
    }
}
