package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginResponse;
import com.AlbertAbuav.Project003Coupons.controllers.model.LogoutDetails;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.security.Information;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.utils.*;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCoupons;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCustomers;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        ResponseEntity<LoginResponse> loggedCompany = restTemplate.postForEntity("http://localhost:8080/client/login", loginDetails, LoginResponse.class);
        System.out.println("The status code response is: " + loggedCompany.getStatusCodeValue());
        System.out.println("This is the Token given to the company: \n" + loggedCompany.getBody().getClientToken());

        Information information = tokenManager.getMap().get(loggedCompany.getBody().getClientToken());
        companyService = (CompanyService) information.getClientFacade();

        loggedToken = loggedCompany.getBody().getClientToken();
        httpHeaders.add("Authorization", loggedToken);
        entity = new HttpEntity<>("parameters", httpHeaders);

        TestUtils.testCompanyInfo("Add Coupon");

        Coupon coupon1 = factoryUtils.createCouponOfACompany(2);
        coupon1.setImage(companyToLogin.getName() + ".jpg");
        System.out.println("this is the coupon to add:");
        chartUtils.printCoupon(coupon1);

        HttpEntity<Coupon> entity1 = new HttpEntity<>(coupon1, httpHeaders);
        ResponseEntity<String> addCoupon = restTemplate.exchange(B_URL + "/coupons", HttpMethod.POST, entity1, String.class);
        System.out.println("The status code response is: " + addCoupon.getStatusCodeValue());

        System.out.println("This is the company details after adding the coupon:");
        chartUtils.printCompany(companyService.getTheLoggedCompanyDetails());

        TestUtils.testCompanyInfo("Update Coupon");

        Coupon couponToUpdate = companyService.getSingleCoupon(5);
        System.out.println("This is the Coupon to update: ");
        chartUtils.printCoupon(couponToUpdate);

        couponToUpdate.setAmount(3);
        couponToUpdate.setDescription("New-Description");

        chartUtils.printCoupon(couponToUpdate);
        HttpEntity<Coupon> entityU = new HttpEntity<>(couponToUpdate, httpHeaders);
        System.out.println(entityU);
        ResponseEntity<String> updateCoupon = restTemplate.exchange(B_URL + "/coupons", HttpMethod.PUT, entityU, String.class);
        System.out.println("The status code response is: " + updateCoupon.getStatusCodeValue());

        System.out.println("This is the company details after updating the coupon:");
        chartUtils.printCompany(companyService.getTheLoggedCompanyDetails());

        TestUtils.testCompanyInfo("Delete Coupon");

        Coupon couponToDelete = companyService.getSingleCoupon(5);
        System.out.println("This is the coupon to delete:");
        chartUtils.printCoupon(couponToDelete);

        ResponseEntity<String> deleteCoupon = restTemplate.exchange(B_URL + "/coupons/" + couponToDelete.getId(), HttpMethod.DELETE, entity, String.class);
        System.out.println(deleteCoupon.getStatusCodeValue());

        System.out.println("This is the company details after deleting the coupon:");
        chartUtils.printCompany(companyService.getTheLoggedCompanyDetails());

        TestUtils.testCompanyInfo("Get all Company Coupons");

        ResponseEntity<Coupon[]> companyCoupons = restTemplate.exchange(B_URL + "/coupons", HttpMethod.GET, entity, Coupon[].class);
        System.out.println("The status code response is: " + companyCoupons.getStatusCodeValue());
        List<Coupon> coupons = Arrays.stream(companyCoupons.getBody()).collect(Collectors.toList());
        chartUtils.printCoupons(coupons);

        TestUtils.testCompanyInfo("Get all Company Coupons of a specific Category");

        System.out.println("The Category to search is: " + coupon1.getCategory());
        ResponseEntity<Coupon[]> categoryCoupons = restTemplate.exchange(B_URL + "/coupons/category/?category=" + coupon1.getCategory(), HttpMethod.GET, entity, Coupon[].class);
        System.out.println("The status code response is: " + categoryCoupons.getStatusCodeValue());
        List<Coupon> coupons2 = Arrays.stream(categoryCoupons.getBody()).collect(Collectors.toList());
        chartUtils.printCoupons(coupons2);

        TestUtils.testCompanyInfo("Get all Company Coupons up to a max price");

        double max = 78.2;
        System.out.println("The max price to search is: " + max);
        try {
            ResponseEntity<Coupon[]> maxPriceCoupons = restTemplate.exchange(B_URL + "/coupons/max-price/?max-price=" + max, HttpMethod.GET, entity, Coupon[].class);
            System.out.println("The status code response is: " + maxPriceCoupons.getStatusCodeValue());
            List<Coupon> coupons3 = Arrays.stream(maxPriceCoupons.getBody()).collect(Collectors.toList());
            chartUtils.printCoupons(coupons3);
        } catch (IllegalStateException illegalStateException) {
            System.out.println(illegalStateException.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        TestUtils.testCompanyInfo("Get the logged Company details");

        ResponseEntity<Company> loggedCompanyDetails = restTemplate.exchange(B_URL + "/company-details", HttpMethod.GET, entity, Company.class);
        System.out.println("The status code response is: " + loggedCompanyDetails.getStatusCodeValue());
        chartUtils.printCompany(loggedCompanyDetails.getBody());

        TestUtils.testCompanyInfo("Get single Coupon");

        ResponseEntity<Coupon> singleCoupon = restTemplate.exchange(B_URL + "/coupons/4", HttpMethod.GET, entity, Coupon.class);
        System.out.println("The status code response is: " + singleCoupon.getStatusCodeValue());
        chartUtils.printCoupon(singleCoupon.getBody());

        TestUtils.testCompanyInfo("Get all Company Customers of a single Coupon by the Coupon id");

        System.out.println("The coupon id is: 4\nThe logged company id is: 2");
        ResponseEntity<ListOfCustomers> companyCustomers = restTemplate.exchange(B_URL + "/company-customers-by-coupon-id/4", HttpMethod.GET, entity, ListOfCustomers.class);
        System.out.println("The status code response is: " + companyCustomers.getStatusCodeValue());
        chartUtils.printCustomers(companyCustomers.getBody().getCustomers());

        TestUtils.testCompanyInfo("Logout the company");

        LogoutDetails logoutDetails = new LogoutDetails(loggedToken);
        HttpEntity<LogoutDetails> logoutEntity = new HttpEntity<>(logoutDetails, httpHeaders);
        ResponseEntity<String> logoutCompany = restTemplate.exchange("http://localhost:8080/client/logout", HttpMethod.DELETE, logoutEntity, String.class);
        System.out.println("The status code response is: " + logoutCompany.getStatusCodeValue());

        System.out.println();
        System.out.println();

    }
}
