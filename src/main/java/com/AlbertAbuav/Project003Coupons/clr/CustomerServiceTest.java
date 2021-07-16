package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.login.ClientType;
import com.AlbertAbuav.Project003Coupons.login.LoginManager;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import com.AlbertAbuav.Project003Coupons.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Order(4)
public class CustomerServiceTest implements CommandLineRunner {

    private final ChartUtils chartUtils;

    /**
     * Invoking the LoginManager to login to the requested facade
     */
    private final LoginManager loginManager;
    private AdminService adminService;
    private CustomerService customerService;
    private CustomerService customerService2;
    private final TokenManager tokenManager;

    @Override
    public void run(String... args) {

        Colors.setPurpleBoldPrint(ArtUtils.CUSTOMER_SERVICE);

        /**
         * Login to Admin Service
         */
        try {
            //(AdminService) loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
            String adminToken = loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
            adminService = (AdminService) tokenManager.getMap().get(adminToken).getClientFacade();
        } catch (invalidCompanyException | invalidCustomerException | invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Login to Customer number 2");

        Customer toConnect2 = null;
        try {
            toConnect2 = adminService.getSingleCustomer(2);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        /**
         * Login to Customer Service
         */
        try {
            //(CustomerService) loginManager.login(toConnect2.getEmail(), toConnect2.getPassword(), ClientType.CUSTOMER);
            String customerToken = loginManager.login(toConnect2.getEmail(), toConnect2.getPassword(), ClientType.CUSTOMER);
            customerService = (CustomerService) tokenManager.getMap().get(customerToken).getClientFacade();
        } catch (invalidCompanyException | invalidCustomerException | invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Login to Customer number 4 (Customer with no coupons)");

        Customer toConnect4 = null;
        try {
            toConnect4 = adminService.getSingleCustomer(4);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        /**
         * Login to Customer Service
         */
        try {
            //(CustomerService) loginManager.login(toConnect4.getEmail(), toConnect4.getPassword(), ClientType.CUSTOMER);
            String customerToken2 = loginManager.login(toConnect4.getEmail(), toConnect4.getPassword(), ClientType.CUSTOMER);
            customerService2 = (CustomerService) tokenManager.getMap().get(customerToken2).getClientFacade();
        } catch (invalidCompanyException | invalidCustomerException | invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Get the logged Customer details");

        System.out.println("The details of the logged customer id-2");
        chartUtils.printCustomer(customerService.getTheLoggedCustomerDetails());
        System.out.println();
        System.out.println("The details of the logged customer id-4");
        chartUtils.printCustomer(customerService2.getTheLoggedCustomerDetails());

        TestUtils.testCustomerInfo("adding Coupon to Customer id-2");

        Coupon couponToAdd1 = null;
        try {
            couponToAdd1 = adminService.getSingleCompany(1).getCoupons().get(1);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The Coupon to add:");
        chartUtils.printCoupon(couponToAdd1);
        try {
            customerService.addCoupon(couponToAdd1);
        } catch (invalidCustomerException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println("This is the Customer after adding the Coupon");
        try {
            chartUtils.printCustomer(adminService.getSingleCustomer(2));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Attempt to add a Coupon that is out of stock");

        Coupon couponToAdd2 = null;
        try {
            couponToAdd2 = adminService.getSingleCompany(1).getCoupons().get(0);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        couponToAdd2.setAmount(0);
        System.out.println("The coupon to add:");
        chartUtils.printCoupon(couponToAdd2);
        System.out.println();
        try {
            customerService.addCoupon(couponToAdd2);
        } catch (invalidCustomerException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Attempt to add a coupon that has been expired");

        couponToAdd2.setAmount(7);
        couponToAdd2.setEndDate(DateUtils.javaDateFromLocalDate(LocalDate.now().minusDays(1)));
        System.out.println("The coupon to add:");
        chartUtils.printCoupon(couponToAdd2);
        System.out.println();
        try {
            customerService.addCoupon(couponToAdd2);
        } catch (invalidCustomerException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Attempt to add a coupon that the Customer already purchased");

        Coupon couponToAdd3 = null;
        try {
            couponToAdd3 = customerService.getAllCustomerCoupons().get(0);
        } catch (invalidCustomerException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The coupon to add:");
        chartUtils.printCoupon(couponToAdd3);
        System.out.println();
        try {
            customerService.addCoupon(couponToAdd3);
        } catch (invalidCustomerException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Get all Customer Coupons");

        try {
            chartUtils.printCoupons(customerService.getAllCustomerCoupons());
        } catch (invalidCustomerException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Get all Customer Coupons of a Customer that have no coupons");

        try {
            chartUtils.printCoupons(customerService2.getAllCustomerCoupons());
        } catch (invalidCustomerException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Get all Customer Coupons of a specific Category");

        try {
            chartUtils.printCoupons(customerService.getAllCustomerCouponsOfSpecificCategory(toConnect2.getCoupons().get(0).getCategory()));
        } catch (invalidCustomerException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Get all Customer Coupons of a specific Category of a Customer that have no coupons");

        try {
            chartUtils.printCoupons(customerService2.getAllCustomerCouponsOfSpecificCategory(toConnect2.getCoupons().get(0).getCategory()));
        } catch (invalidCustomerException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Get all Customer Coupons up to a maximum Price");

        System.out.println("The customer coupons:");
        chartUtils.printCoupons(toConnect2.getCoupons());
        double maxPrice = toConnect2.getCoupons().get(0).getPrice();
        System.out.println("The maxPrice is: " + maxPrice);
        System.out.println("Getting the coupons:");
        try {
            chartUtils.printCoupons(customerService.getAllCustomerCouponsUpToMaxPrice(maxPrice));
        } catch (invalidCustomerException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Get all Customer Coupons up to a maximum Price of a Customer that have no coupons");

        try {
            chartUtils.printCoupons(customerService2.getAllCustomerCouponsUpToMaxPrice(toConnect2.getCoupons().get(0).getPrice()));
        } catch (invalidCustomerException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCustomerInfo("Find all Customers by a Coupon ID-3");
        chartUtils.printCustomers(customerService.findAllCustomersByCouponId(3));

        System.out.println();
    }

}
