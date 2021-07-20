package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
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
@Order(7)
public class AdminControllerTest implements CommandLineRunner {

    private final ChartUtils chartUtils;
    private final static String B_URL = "http://localhost:8080/admin-service";

    private final RestTemplate restTemplate;
    private AdminService adminService;

    @Override
    public void run(String... args) throws Exception {

        Colors.setGreenBoldPrint(ArtUtils.ADMIN_CONTROLLER);

        TestUtils.testAdminInfo("Login to Admin and receive a token");

        LoginDetails loginDetails = new LoginDetails("admin@admin.com", "admin");
        ResponseEntity<String> loggedAdmin = restTemplate.postForEntity(B_URL + "/login", loginDetails, String.class);
        System.out.println("The status code response is: " + loggedAdmin.getStatusCodeValue());
        System.out.println("This is the Token given to the admin: \n" + loggedAdmin.getBody());
        System.out.println();

        TestUtils.testAdminInfo("Add Company");

        TestUtils.testAdminInfo("Update Company");

        TestUtils.testAdminInfo("Delete Company");

        TestUtils.testAdminInfo("Get all Companies");

        TestUtils.testAdminInfo("Get single Company");

        TestUtils.testAdminInfo("Add Customer");

        TestUtils.testAdminInfo("Update Customer");

        TestUtils.testAdminInfo("Delete Customer");

        TestUtils.testAdminInfo("Get all Customers");

        TestUtils.testAdminInfo("Get single Customer");

        TestUtils.testAdminInfo("Logout the Admin");

    }
}
