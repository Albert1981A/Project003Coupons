package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.login.ClientType;
import com.AlbertAbuav.Project003Coupons.login.LoginManager;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(10)
public class DefaultObjects implements CommandLineRunner {

    private final FactoryUtils factoryUtils;
    private final ChartUtils chartUtils;
    private final LoginManager loginManager;
    private final TokenManager tokenManager;
    private AdminService adminService;
    private CompanyService companyService;

    @Override
    public void run(String... args) {

        Colors.setGreenBoldPrint(ArtUtils.DEFAULT_OBJECTS);

        try {
            //(AdminService) loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
            String adminToken = loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
            adminService = (AdminService) tokenManager.getMap().get(adminToken).getClientFacade();
        } catch (invalidCompanyException | invalidCustomerException | invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Adding Default Company");

        Coupon coupon1 = factoryUtils.createCouponOfACompany(11);
        Coupon coupon2 = factoryUtils.createCouponOfACompany(11);
        Coupon coupon3 = factoryUtils.createCouponOfACompany(11);

        try {
            Company company = factoryUtils.createCompany();
            System.out.println(company);
            company.setEmail("company@company.com");
            company.setPassword("company");
            coupon1.setImage(company.getName() + ".jpg");
            coupon2.setImage(company.getName() + ".jpg");
            coupon3.setImage(company.getName() + ".jpg");
            List<Coupon> coupons = new ArrayList<>(Arrays.asList(coupon1, coupon2, coupon3));

            company.setCoupons(coupons);

            System.out.println("This is the company to add: ");
            chartUtils.printCompany(company);


            adminService.addCompany(company);
            System.out.println("Company after adding: ");
            chartUtils.printCompanies(adminService.getAllCompanies());
        } catch (IOException | invalidAdminException e) {
            System.out.println(e.getMessage());
        }



        TestUtils.testCompanyInfo("Login to Company number - 2");

        Company toConnect = null;
        try {
            toConnect = adminService.getSingleCompany(2);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        /**
         * Login to Company Service
         */
        try {
            //(CompanyService) loginManager.login(toConnect.getEmail(), toConnect.getPassword(), ClientType.COMPANY);
            String companyToken = loginManager.login(toConnect.getEmail(), toConnect.getPassword(), ClientType.COMPANY);
            companyService = (CompanyService) tokenManager.getMap().get(companyToken).getClientFacade();
        } catch (invalidCompanyException | invalidCustomerException | invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("The details of the logged Company id-2");
        Company loggedCompany = companyService.getTheLoggedCompanyDetails();
        chartUtils.printCompany(loggedCompany);

        TestUtils.testAdminInfo("Adding Default Customer");

        Coupon coupon4 = loggedCompany.getCoupons().get(1);

        Customer customer = null;
        try {
            customer = factoryUtils.createCustomer();
            customer.setFirstName("Customer-First");
            customer.setLastName("Customer-Last");
            customer.setEmail("customer@customer.com");
            customer.setPassword("customer");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        List<Coupon> customerCoupons = new ArrayList<>(Arrays.asList(coupon2, coupon3, coupon4));

        try {
            adminService.addCustomer(customer);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        customer.setCoupons(customerCoupons);

        System.out.println("This is the Customer to add: ");
        chartUtils.printCustomer(customer);

        try {
            adminService.updateCustomer(customer);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();

    }

}
