package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LogoutDetails;
import com.AlbertAbuav.Project003Coupons.security.Information;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.utils.*;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCoupons;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Order(8)
public class CompanyControllerTest implements CommandLineRunner {

    private final ChartUtils chartUtils;
    private final static String B_URL = "http://localhost:8080/company-service";
    private static String loggedToken;
    private static HttpHeaders httpHeaders = new HttpHeaders();
    private static HttpEntity<String> entity;

    private final RestTemplate restTemplate;
    private final FactoryUtils factoryUtils;
    private final AdminService adminService;
    private final TokenManager tokenManager;
    private CompanyService companyService;


    @Override
    public void run(String... args) throws Exception {

        Colors.setCyanBoldPrint(ArtUtils.COMPANY_CONTROLLER);

        TestUtils.testCompanyInfo("Login to a Company and receive a token");

        Company companyToLogin = adminService.getSingleCompany(2);
        LoginDetails loginDetails = new LoginDetails(companyToLogin.getEmail(), companyToLogin.getPassword());
        ResponseEntity<String> loggedCompany = restTemplate.postForEntity(B_URL + "/login", loginDetails, String.class);
        System.out.println("The status code response is: " + loggedCompany.getStatusCodeValue());
        System.out.println("This is the Token given to the company: \n" + loggedCompany.getBody());

        Information information = tokenManager.getMap().get(loggedCompany.getBody());
        companyService = (CompanyService) information.getClientFacade();

        loggedToken = loggedCompany.getBody();
        httpHeaders.add("Authorization", loggedToken);
        entity = new HttpEntity<>("parameters", httpHeaders);

        TestUtils.testCompanyInfo("Add Coupon");

        Coupon coupon1 = factoryUtils.createCouponOfACompany(2);
        System.out.println("this is the coupon to add:");
        chartUtils.printCoupon(coupon1);

        HttpEntity<Coupon> entity1 = new HttpEntity<>(coupon1, httpHeaders);
        ResponseEntity<String> addCoupon = restTemplate.exchange(B_URL + "/coupons", HttpMethod.POST, entity1, String.class);
        System.out.println("The status code response is: " + addCoupon.getStatusCodeValue());

        System.out.println("This is the company details after adding the coupon:");
        chartUtils.printCompany(companyService.getTheLoggedCompanyDetails());

        TestUtils.testCompanyInfo("Update Coupon");

        TestUtils.testCompanyInfo("Delete Coupon");

        TestUtils.testCompanyInfo("Get all Company Coupons");

        ResponseEntity<ListOfCoupons> companyCoupons = restTemplate.exchange(B_URL + "/coupons", HttpMethod.GET, entity, ListOfCoupons.class);
        System.out.println("The status code response is: " + companyCoupons.getStatusCodeValue());
        chartUtils.printCoupons(companyCoupons.getBody().getCoupons());

        TestUtils.testCompanyInfo("Get all Company Coupons of a specific Category");

        System.out.println("The Category to search is: " + coupon1.getCategory());
        ResponseEntity<ListOfCoupons> categoryCoupons = restTemplate.exchange(B_URL + "/coupons/category/?category=" + coupon1.getCategory(), HttpMethod.GET, entity, ListOfCoupons.class);
        System.out.println("The status code response is: " + categoryCoupons.getStatusCodeValue());
        chartUtils.printCoupons(categoryCoupons.getBody().getCoupons());

        TestUtils.testCompanyInfo("Get all Company Coupons up to a max price");

        TestUtils.testCompanyInfo("Get the logged Company details");

        ResponseEntity<Company> loggedCompanyDetails = restTemplate.exchange(B_URL + "/company-details", HttpMethod.GET, entity, Company.class);
        System.out.println("The status code response is: " + loggedCompanyDetails.getStatusCodeValue());
        chartUtils.printCompany(loggedCompanyDetails.getBody());

        TestUtils.testCompanyInfo("Get single Coupon");

        ResponseEntity<Coupon> singleCoupon = restTemplate.exchange(B_URL + "/coupons/4", HttpMethod.GET, entity, Coupon.class);
        System.out.println("The status code response is: " + singleCoupon.getStatusCodeValue());
        chartUtils.printCoupon(singleCoupon.getBody());

        TestUtils.testCompanyInfo("Get all Company Customers of a single Coupon by the Coupon id");

        TestUtils.testCompanyInfo("Logout the company");

        LogoutDetails logoutDetails = new LogoutDetails(loggedToken);
        HttpEntity<LogoutDetails> logoutEntity = new HttpEntity<>(logoutDetails);
        ResponseEntity<String> logoutCompany = restTemplate.exchange(B_URL + "/logout", HttpMethod.DELETE, logoutEntity , String.class);
        System.out.println("The status code response is: " + logoutCompany.getStatusCodeValue());

        System.out.println();
        System.out.println();

    }
}
