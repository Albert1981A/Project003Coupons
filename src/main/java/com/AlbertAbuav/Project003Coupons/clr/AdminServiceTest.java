package com.AlbertAbuav.Project003Coupons.clr;

import com.AlbertAbuav.Project003Coupons.beans.Company;
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

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(2)
public class AdminServiceTest implements CommandLineRunner {

    private final ChartUtils chartUtils;
    private final FactoryUtils factoryUtils;

    /**
     * Invoking the LoginManager to login to the requested facade
     */
    private final LoginManager loginManager;
    private AdminService adminService;
    private final TokenManager tokenManager;

    @Override
    public void run(String... args) {

        /**
         * Login to Admin Service
         */
        Colors.setGreenBoldPrint(ArtUtils.ADMIN_SERVICE);

        TestUtils.testAdminInfo("Login to Admin Service");

        try {
            //(AdminService) loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
            String adminToken = loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
            adminService = (AdminService) tokenManager.getMap().get(adminToken).getClientFacade();
        } catch (invalidCompanyException | invalidCustomerException | invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Attempting to add a Company with an existing name");

        try {
            Company companyNew = factoryUtils.createCompany();
            System.out.println(companyNew.getImage().getId());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Company companyToTest1 = null;
        try {
            companyToTest1 = Company.builder()
                    .name(adminService.getSingleCompany(7).getName())
                    .password("1234")
                    .email("sample@sample.com")
                    .build();
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        try {
            adminService.addCompany(companyToTest1);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Attempting to add a Company with an existing email");

        Company companyToTest2 = null;
        try {
            companyToTest2 = Company.builder()
                    .name("sample")
                    .password("1234")
                    .email(adminService.getSingleCompany(7).getEmail())
                    .build();
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        try {
            adminService.addCompany(companyToTest2);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Attempting to update a Company email");

        Company companyToUpdate1 = null;
        try {
            companyToUpdate1 = adminService.getSingleCompany(7);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The Company before changing the email:");
        chartUtils.printCompany(companyToUpdate1);
        companyToUpdate1.setEmail("email@email.com");
        try {
            adminService.updateCompany(companyToUpdate1);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The Company after changing the email:");
        try {
            chartUtils.printCompany(adminService.getSingleCompany(7));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Attempting to update a Company id");

        Company companyToUpdate2 = null;
        try {
            companyToUpdate2 = adminService.getSingleCompany(8);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The Company before changing the id:");
        chartUtils.printCompany(companyToUpdate2);
        companyToUpdate2.setId(3);
        System.out.println("Attempting to change the id:");
        try {
            adminService.updateCompany(companyToUpdate2);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Attempting to update a Company name (using new name)");

        Company companyToUpdate3 = null;
        try {
            companyToUpdate3 = adminService.getSingleCompany(9);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The Company before changing the name:");
        chartUtils.printCompany(companyToUpdate3);
        companyToUpdate3.setName("New-Name");
        System.out.println("Attempting to change the name:");
        try {
            adminService.updateCompany(companyToUpdate3);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Attempting to update a Company name (using an existing name)");

        Company companyToUpdate4 = null;
        try {
            companyToUpdate4 = adminService.getSingleCompany(10);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The Company before changing the name:");
        chartUtils.printCompany(companyToUpdate4);
        try {
            companyToUpdate4.setName(adminService.getSingleCompany(3).getName());
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Attempting to change the name:");
        try {
            adminService.updateCompany(companyToUpdate4);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Get all Companies");

        try {
            chartUtils.printCompanies(adminService.getAllCompanies());
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Get single Company");

        try {
            chartUtils.printCompany(adminService.getSingleCompany(3));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Attempting to add a Customer with an existing email");

        Customer customerToAdd1 = null;
        try {
            customerToAdd1 = Customer.builder()
                    .firstName("Sample")
                    .lastName("Sample")
                    .email(adminService.getSingleCustomer(4).getEmail())
                    .password("1234")
                    .build();
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("This is the customer to add:");
        chartUtils.printCustomer(customerToAdd1);
        System.out.println("Attempting to add the Customer:");
        try {
            adminService.addCustomer(customerToAdd1);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Update Customer name");

        Customer customerToUpdate1 = null;
        try {
            customerToUpdate1 = adminService.getSingleCustomer(9);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The Customer to update hes name");
        chartUtils.printCustomer(customerToUpdate1);
        customerToUpdate1.setFirstName("New-Name");
        try {
            adminService.updateCustomer(customerToUpdate1);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Customer after the update:");
        try {
            chartUtils.printCustomer(adminService.getSingleCustomer(9));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Attempting to update Customer id");

        Customer customerToUpdate2 = null;
        try {
            customerToUpdate2 = adminService.getSingleCustomer(10);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("The Customer to update hes id");
        chartUtils.printCustomer(customerToUpdate2);
        customerToUpdate2.setId(4);
        System.out.println("Attempting to update hes id");
        try {
            adminService.updateCustomer(customerToUpdate2);
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Get all Customers");

        try {
            chartUtils.printCustomers(adminService.getAllCustomers());
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.testAdminInfo("Get single Customer");

        try {
            chartUtils.printCustomer(adminService.getSingleCustomer(3));
        } catch (invalidAdminException e) {
            System.out.println(e.getMessage());
        }


    }
}
