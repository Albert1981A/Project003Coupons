package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Category;
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

import java.time.LocalDate;
import java.util.List;

//@Component
@RequiredArgsConstructor
@Order(3)
public class CompanyServiceTest implements CommandLineRunner {

    private final ChartUtils chartUtils;

    /**
     * Invoking the LoginManager to login to the requested facade
     */
    private final LoginManager loginManager;
    private AdminService adminService;
    private CompanyService companyService;
    private CompanyService companyService2;
    private final TokenManager tokenManager;

    @Override
    public void run(String... args) {

        Colors.setCyanBoldPrint(ArtUtils.COMPANY_SERVICE);

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

        TestUtils.testCompanyInfo("Login to Company number - 4");

        Company toConnect2 = null;
        try {
            toConnect2 = adminService.getSingleCompany(4);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        /**
         * Login to Company Service
         */
        try {
            //(CompanyService) loginManager.login(toConnect2.getEmail(), toConnect2.getPassword(), ClientType.COMPANY);
            String companyToken2 = loginManager.login(toConnect2.getEmail(), toConnect2.getPassword(), ClientType.COMPANY);
            companyService2 = (CompanyService) tokenManager.getMap().get(companyToken2).getClientFacade();
        } catch (invalidCompanyException | invalidCustomerException | invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Get the logged Company Details");

        System.out.println("The details of the logged Company id-2");
        chartUtils.printCompany(companyService.getTheLoggedCompanyDetails());
        System.out.println("The details of the logged Company id-4");
        chartUtils.printCompany(companyService2.getTheLoggedCompanyDetails());

        TestUtils.testCompanyInfo("Get Single Coupon");

        try {
            chartUtils.printCoupon(companyService.getSingleCoupon(4));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Get Single Coupon that doesn't belong to the connected company");

        try {
            chartUtils.printCoupon(companyService.getSingleCoupon(1));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Attempting to get a Single Coupon that doesn't appears in the data base");

        try {
            chartUtils.printCoupon(companyService.getSingleCoupon(12));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Adding a new Coupon to Company id - 3");

        Coupon couponToAdd1 = Coupon.builder()
                .companyID(3)
                .category(Category.VACATIONS_ABROAD)
                .title("New Title1")
                .description("New Description1")
                .startDate(DateUtils.javaDateFromLocalDate(LocalDate.now().minusDays(3)))
                .endDate(DateUtils.javaDateFromLocalDate(LocalDate.now().plusDays(7)))
                .amount((int) (Math.random() * 21) + 30)
                .price((int) (Math.random() * 41) + 60)
                .image("New Image1")
                .build();
        System.out.println("Attempting to add this Coupon: ");
        chartUtils.printCoupon(couponToAdd1);
        try {
            companyService.addCoupon(couponToAdd1);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The Companies Coupons after adding the new Coupon:");
        try {
            chartUtils.printCompany(adminService.getSingleCompany(3));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Attempting to add a Coupon to the company that have the same title to an existing coupon of another company");

        Coupon sampleCoupon = null;
        try {
            sampleCoupon = companyService.getSingleCoupon(4);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        Coupon couponToAdd2 = Coupon.builder()
                .companyID(3)
                .category(Category.HOUSEHOLD_PRODUCTS)
                .title(sampleCoupon.getTitle())
                .description("New Description2")
                .startDate(DateUtils.javaDateFromLocalDate(LocalDate.now().minusDays(3)))
                .endDate(DateUtils.javaDateFromLocalDate(LocalDate.now().plusDays(7)))
                .amount((int) (Math.random() * 21) + 30)
                .price((int) (Math.random() * 41) + 60)
                .image("New Image2")
                .build();
        System.out.println("Attempting to add this coupon: ");
        chartUtils.printCoupon(couponToAdd2);
        try {
            companyService.addCoupon(couponToAdd2);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The Companies Coupons after adding the new Coupon:");
        try {
            chartUtils.printCompany(adminService.getSingleCompany(3));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Attempting to add a Coupon to the company that have the same title to an existing coupon of this company");

        Coupon couponToAdd3 = Coupon.builder()
                .companyID(sampleCoupon.getCompanyID())
                .category(Category.FOOD_PRODUCTS)
                .title(sampleCoupon.getTitle())
                .description("New Description3")
                .startDate(DateUtils.javaDateFromLocalDate(LocalDate.now().minusDays(3)))
                .endDate(DateUtils.javaDateFromLocalDate(LocalDate.now().plusDays(7)))
                .amount((int) (Math.random() * 21) + 30)
                .price((int) (Math.random() * 41) + 60)
                .image("New Image3")
                .build();
        System.out.println("Attempting to add this coupon: ");
        chartUtils.printCoupon(couponToAdd3);
        try {
            companyService.addCoupon(couponToAdd3);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Attempting to add a Coupon to a Company that doesn't exist in the data base");

        Coupon couponToAdd4 = Coupon.builder()
                .companyID(12)
                .category(Category.VACATIONS_ABROAD)
                .title("New Title4")
                .description("New Description4")
                .startDate(DateUtils.javaDateFromLocalDate(LocalDate.now().minusDays(3)))
                .endDate(DateUtils.javaDateFromLocalDate(LocalDate.now().plusDays(7)))
                .amount((int) (Math.random() * 21) + 30)
                .price((int) (Math.random() * 41) + 60)
                .image("New Image4")
                .build();
        System.out.println("Attempting to add this Coupon: ");
        chartUtils.printCoupon(couponToAdd1);
        try {
            companyService.addCoupon(couponToAdd4);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Update a company coupon start date");

        Coupon couponToUpdate1 = null;
        try {
            couponToUpdate1 = companyService.getSingleCoupon(4);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Trying to update the \"startDate\" of coupon id-4 to be \"current date\": ");
        chartUtils.printCoupon(couponToUpdate1);
        couponToUpdate1.setStartDate(DateUtils.javaDateFromLocalDate(LocalDate.now()));
        System.out.println("The Coupon was set for update:");
        chartUtils.printCoupon(couponToUpdate1);
        try {
            companyService.updateCoupon(couponToUpdate1);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The coupon after the update:");
        try {
            chartUtils.printCoupon(companyService.getSingleCoupon(4));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Update a company coupon amount to be 23");

        System.out.println("Trying to update the \"amount\" of coupon id-4 to be \"23\": ");
        chartUtils.printCoupon(couponToUpdate1);
        couponToUpdate1.setAmount(23);
        System.out.println("The Coupon was set for update:");
        chartUtils.printCoupon(couponToUpdate1);
        try {
            companyService.updateCoupon(couponToUpdate1);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The coupon after the update:");
        try {
            chartUtils.printCoupon(companyService.getSingleCoupon(4));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Attempt to Update a company coupons title");

        System.out.println("Trying to update the \"title\" of coupon id-4:");
        chartUtils.printCoupon(couponToUpdate1);
        couponToUpdate1.setTitle("New-Title");
        System.out.println("The Coupon was set for update:");
        chartUtils.printCoupon(couponToUpdate1);
        try {
            companyService.updateCoupon(couponToUpdate1);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The coupon after the update:");
        try {
            chartUtils.printCoupon(companyService.getSingleCoupon(4));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Attempt to Update a company coupons id");

        System.out.println("Trying to update the \"id\" of coupon id-4 to coupon id-5:");
        chartUtils.printCoupon(couponToUpdate1);
        couponToUpdate1.setId(5);
        System.out.println("The Coupon was set for update:");
        chartUtils.printCoupon(couponToUpdate1);
        try {
            companyService.updateCoupon(couponToUpdate1);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("The coupon after the update:");
        try {
            chartUtils.printCoupon(companyService.getSingleCoupon(4));
            chartUtils.printCoupon(companyService.getSingleCoupon(5));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Attempt to Update a coupons company id");

        try {
            couponToUpdate1 = companyService.getSingleCoupon(4);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Trying to update the \"companyID\" of coupon id-4 to be companyID-3: ");
        chartUtils.printCoupon(couponToUpdate1);
        couponToUpdate1.setCompanyID(3);
        System.out.println("The Coupon was set for update:");
        chartUtils.printCoupon(couponToUpdate1);
        try {
            companyService.updateCoupon(couponToUpdate1);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The coupon after the update:");
        try {
            chartUtils.printCoupon(companyService.getSingleCoupon(4));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Attempt to Update a coupon id and Title");

        try {
            couponToUpdate1 = companyService.getSingleCoupon(4);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Trying to update the \"coupon id\" of and the \"title\" of coupon id-4: ");
        chartUtils.printCoupon(couponToUpdate1);
        couponToUpdate1.setId(5);
        couponToUpdate1.setTitle("New-Title2");
        System.out.println("The Coupon was set for update:");
        chartUtils.printCoupon(couponToUpdate1);
        try {
            companyService.updateCoupon(couponToUpdate1);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The coupon after the update:");
        try {
            chartUtils.printCoupon(companyService.getSingleCoupon(4));
            chartUtils.printCoupon(companyService.getSingleCoupon(5));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Get all the Company Coupons");

        chartUtils.printCoupons(companyService.getAllCompanyCoupons());

        TestUtils.testCompanyInfo("Get all the Company Coupons of a specific Category");

        Category testCategory = null;
        try {
            testCategory = companyService.getSingleCoupon(5).getCategory();
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("This is the Category for testing: " + testCategory + ", and this is it's ordinal number: " + testCategory.ordinal());
        try {
            chartUtils.printCoupons(companyService.getAllCompanyCouponsOfSpecificCategory(testCategory));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Get all the Company Coupons of a specific Category of a Company that have no Coupons");

        try {
            chartUtils.printCoupons(companyService2.getAllCompanyCouponsOfSpecificCategory(testCategory));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Get all Company Coupons up to a maximum price");

        double maxPrice = 0;
        try {
            maxPrice = companyService.getSingleCoupon(5).getPrice() + 1;
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The max price is: " + maxPrice);

        try {
            chartUtils.printCoupons(companyService.getAllCompanyCouponsUpToMaxPrice(maxPrice));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Get all Company Coupons up to a maximum price of a Company that have no Coupons");

        try {
            chartUtils.printCoupons(companyService2.getAllCompanyCouponsUpToMaxPrice(maxPrice));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Get all Company Coupons up to a maximum price that doesn't exists");

        try {
            chartUtils.printCoupons(companyService.getAllCompanyCouponsUpToMaxPrice(1.2));
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Get all the Company Customers of a single Coupon by Coupon ID");

        System.out.println("Which customers have coupon id number 5:");
        List<Customer> companiesCustomers = null;
        try {
            companiesCustomers = companyService.getAllCompanyCustomersOfASingleCouponByCouponId(5);
            chartUtils.printCustomers(companiesCustomers);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testCompanyInfo("Get all the Company Customers of a single Coupon by a Coupon ID the doesn't belong to the logged company");

        System.out.println("Which customers have coupon id number 1 (This coupon doesn't belong to the logged company):");
        companiesCustomers = null;
        try {
            companiesCustomers = companyService.getAllCompanyCustomersOfASingleCouponByCouponId(1);
            chartUtils.printCustomers(companiesCustomers);
        } catch (invalidCompanyException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println();
    }
}
