package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.utils.ArtUtils;
import com.AlbertAbuav.Project003Coupons.utils.ChartUtils;
import com.AlbertAbuav.Project003Coupons.utils.Colors;
import com.AlbertAbuav.Project003Coupons.utils.TestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

//@Component
@RequiredArgsConstructor
@Order(8)
public class CompanyControllerTest implements CommandLineRunner {

    private final ChartUtils chartUtils;
    private final static String B_URL = "http://localhost:8080/company-service";

    private final RestTemplate restTemplate;
    private final CompanyService companyService;
    private final AdminService adminService;

    @Override
    public void run(String... args) throws Exception {

        Colors.setCyanBoldPrint(ArtUtils.COMPANY_CONTROLLER);

        TestUtils.testCompanyInfo("Login to a Company and receive a token");

        Company companyToLogin = adminService.getSingleCompany(2);
        LoginDetails loginDetails = new LoginDetails(companyToLogin.getEmail(), companyToLogin.getPassword());
        ResponseEntity<String> loggedCompany = restTemplate.postForEntity(B_URL + "/login", loginDetails, String.class);
        System.out.println("The status code response is: " + loggedCompany.getStatusCodeValue());
        System.out.println("This is the Token given to the company: \n" + loggedCompany.getBody());
        System.out.println();

        TestUtils.testCompanyInfo("Add Coupon");

        TestUtils.testCompanyInfo("Update Coupon");

        TestUtils.testCompanyInfo("Delete Coupon");

        TestUtils.testCompanyInfo("Get all Company Coupons");

        TestUtils.testCompanyInfo("Get all Company Coupons of a specific Category");

        TestUtils.testCompanyInfo("Get all Company Coupons up to a max price");

        TestUtils.testCompanyInfo("Get the logged Company details");

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", loggedCompany.getBody());

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", loggedCompany.getBody());
        //HttpEntity<String> entity = new HttpEntity<>("parameters", httpHeaders);

        ResponseEntity<Company> loggedCompanyDetails = restTemplate.exchange(B_URL + "/company-details", HttpMethod.GET, entity, Company.class);
        System.out.println("The status code response is: " + loggedCompanyDetails.getStatusCodeValue());
        chartUtils.printCompany(loggedCompanyDetails.getBody());

        TestUtils.testCompanyInfo("Get single Coupon");

        TestUtils.testCompanyInfo("Get all Company Customers of a single Coupon by the Coupon id");

        TestUtils.testCompanyInfo("Logout the company");

    }
}
