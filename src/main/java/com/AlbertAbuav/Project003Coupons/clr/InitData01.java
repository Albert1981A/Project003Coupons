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
import com.AlbertAbuav.Project003Coupons.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class InitData01 implements CommandLineRunner {

    private final FactoryUtils factoryUtils;
    private final ChartUtils chartUtils;

    /**
     * Invoking the LoginManager to login to the requested facade
     */
    private final LoginManager loginManager;
    private AdminService adminService;
    private final TokenManager tokenManager;

    @Override
    public void run(String... args) {

        Colors.setRedBoldBrightPrint("START");
        Colors.separation();

        Colors.setGreenBoldPrint(ArtUtils.INIT_DATA_01);


        TestUtils.testAdminInfo("Connecting to AdminService");

        try {
            //(AdminService) loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
            String adminToken = loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
            adminService = (AdminService) tokenManager.getMap().get(adminToken).getClientFacade();
        } catch (invalidCompanyException | invalidCustomerException | invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Get all Companies when their are no Companies in the data base");

        try {
            chartUtils.printCompanies(adminService.getAllCompanies());
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Get single Company when their are no Companies in the data base");

        try {
            chartUtils.printCompany(adminService.getSingleCompany(3));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Get all Customers when their are no Customers in the data base");
        try {
            chartUtils.printCustomers(adminService.getAllCustomers());
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Get s single Customer when their are no Customers in the data base");

        try {
            chartUtils.printCustomer(adminService.getSingleCustomer(3));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Adding Companies");

        Coupon coupon1 = factoryUtils.createCouponOfACompany(1);
        Coupon coupon2 = factoryUtils.createCouponOfACompany(1);
        Coupon coupon3 = factoryUtils.createCouponOfACompany(1);
        Coupon coupon4 = factoryUtils.createCouponOfACompany(2);
        Coupon coupon5 = factoryUtils.createCouponOfACompany(2);
        Coupon coupon6 = factoryUtils.createCouponOfACompany(2);
        Coupon coupon7 = factoryUtils.createCouponOfACompany(3);
        Coupon coupon8 = factoryUtils.createCouponOfACompany(3);
        Coupon coupon9 = factoryUtils.createCouponOfACompany(3);

        List<Coupon> coupons1 = new ArrayList<>(Arrays.asList(coupon1, coupon2, coupon3));
        List<Coupon> coupons2 = new ArrayList<>(Arrays.asList(coupon4, coupon5, coupon6));
        List<Coupon> coupons3 = new ArrayList<>(Arrays.asList(coupon7, coupon8, coupon9));

        try {
            Company company1 = factoryUtils.createCompany();
            coupons1.forEach(coupon -> coupon.setImage(company1.getName() + ".jpg"));
            System.out.println(coupons1);
            company1.setCoupons(coupons1);
            adminService.addCompany(company1);
            Company company2 = factoryUtils.createCompany();
            coupons2.forEach(coupon -> coupon.setImage(company2.getName() + ".jpg"));
            company2.setCoupons(coupons2);
            adminService.addCompany(company2);
            Company company3 = factoryUtils.createCompany();
            coupons3.forEach(coupon -> coupon.setImage(company3.getName() + ".jpg"));
            company3.setCoupons(coupons3);
            adminService.addCompany(company3);
            Company company4 = factoryUtils.createCompany();
            adminService.addCompany(company4);
            Company company5 = factoryUtils.createCompany();
            adminService.addCompany(company5);
            Company company6 = factoryUtils.createCompany();
            adminService.addCompany(company6);
            Company company7 = factoryUtils.createCompany();
            adminService.addCompany(company7);
            Company company8 = factoryUtils.createCompany();
            adminService.addCompany(company8);
            Company company9 = factoryUtils.createCompany();
            adminService.addCompany(company9);
            Company company10 = factoryUtils.createCompany();
            adminService.addCompany(company10);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }



        try {
            chartUtils.printCompanies(adminService.getAllCompanies());
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Adding Customers");

        Customer customer1 = factoryUtils.createCustomer();
        Customer customer2 = factoryUtils.createCustomer();
        Customer customer3 = factoryUtils.createCustomer();
        Customer customer4 = factoryUtils.createCustomer();
        Customer customer5 = factoryUtils.createCustomer();
        Customer customer6 = factoryUtils.createCustomer();
        Customer customer7 = factoryUtils.createCustomer();
        Customer customer8 = factoryUtils.createCustomer();
        Customer customer9 = factoryUtils.createCustomer();
        Customer customer10 = factoryUtils.createCustomer();


        List<Coupon> customerCoupons1 = new ArrayList<>(Arrays.asList(coupon2, coupon3, coupon4, coupon8));
        List<Coupon> customerCoupons2 = new ArrayList<>(Arrays.asList(coupon3, coupon4, coupon5, coupon9));
        List<Coupon> customerCoupons3 = new ArrayList<>(Arrays.asList(coupon4, coupon5, coupon6, coupon7));

        try {
            adminService.addCustomer(customer1);
            adminService.addCustomer(customer2);
            adminService.addCustomer(customer3);
            adminService.addCustomer(customer4);
            adminService.addCustomer(customer5);
            adminService.addCustomer(customer6);
            adminService.addCustomer(customer7);
            adminService.addCustomer(customer8);
            adminService.addCustomer(customer9);
            adminService.addCustomer(customer10);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        customer1.setCoupons(customerCoupons1);
        customer2.setCoupons(customerCoupons2);
        customer3.setCoupons(customerCoupons3);

        try {
            adminService.updateCustomer(customer1);
            adminService.updateCustomer(customer2);
            adminService.updateCustomer(customer3);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        try {
            chartUtils.printCustomers(adminService.getAllCustomers());
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();

    }

}
