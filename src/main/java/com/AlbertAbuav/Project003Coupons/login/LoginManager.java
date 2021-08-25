package com.AlbertAbuav.Project003Coupons.login;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginResponse;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.security.Information;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import com.AlbertAbuav.Project003Coupons.serviceImpl.AdminServiceImpl;
import com.AlbertAbuav.Project003Coupons.serviceImpl.ClientFacade;
import com.AlbertAbuav.Project003Coupons.serviceImpl.CompanyServiceImpl;
import com.AlbertAbuav.Project003Coupons.serviceImpl.CustomerServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
@RequiredArgsConstructor
@Data
public class LoginManager {

    private final ApplicationContext ctx;
    private final TokenManager tokenManager;
    private String token;
    private ClientFacade clientFacade;

    /**
     * This method defines what type of client is trying to connect.
     * It will define whether he is allowed to enter the facade layer.
     * @param email String
     * @param password String
     * @param clientType String
     * @return String
     */
    public String login(String email, String password, ClientType clientType) throws invalidCompanyException, invalidCustomerException, invalidAdminException {
        switch (clientType) {
            case ADMINISTRATOR:
                AdminService adminService = ctx.getBean(AdminService.class);
                if (((AdminServiceImpl) adminService).login(email, password)) {
                    //==> return Token
                    return tokenManager.addToken((AdminServiceImpl) adminService);
                }
                break;
            case COMPANY:
                CompanyService companyService = ctx.getBean(CompanyService.class);
                if (((CompanyServiceImpl) companyService).login(email, password)) {
                    //==> return Token
                    return tokenManager.addToken((CompanyServiceImpl) companyService);
                }
                break;
            case CUSTOMER:
                CustomerService customerService = ctx.getBean(CustomerService.class);
                if (((CustomerServiceImpl) customerService).login(email, password)) {
                    //==> return Token
                    return tokenManager.addToken((CustomerServiceImpl) customerService);
                }
                break;
        }
        throw new SecurityException("Invalid email or password");
    }

    public LoginResponse controllerLogin(String email, String password, ClientType clientType) throws invalidCompanyException, invalidCustomerException, invalidAdminException {
        switch (clientType) {
            case ADMINISTRATOR:
                AdminService adminService = ctx.getBean(AdminService.class);
                if (((AdminServiceImpl) adminService).login(email, password)) {
                    clientFacade = ((AdminServiceImpl) adminService);
                    token = tokenManager.addToken(clientFacade);
                    LoginResponse loginResponse = new LoginResponse();
                    loginResponse.setClientEmail("admin@admin.com");
                    loginResponse.setClientName("Admin");
                    loginResponse.setClientType(ClientType.ADMINISTRATOR);
                    loginResponse.setClientToken(token);
                    return loginResponse;
                }
                break;
            case COMPANY:
                CompanyService companyService = ctx.getBean(CompanyService.class);
                if (((CompanyServiceImpl) companyService).login(email, password)) {
                    clientFacade = ((CompanyServiceImpl) companyService);
                    token = tokenManager.addToken(clientFacade);
                    Company company = companyService.getTheLoggedCompanyDetails();
                    LoginResponse loginResponse = new LoginResponse();
                    loginResponse.setClientId(company.getId());
                    loginResponse.setClientEmail(company.getEmail());
                    loginResponse.setClientName(company.getName());
                    loginResponse.setClientType(ClientType.COMPANY);
                    loginResponse.setClientToken(token);
                    return loginResponse;
                }
                break;
            case CUSTOMER:
                CustomerService customerService = ctx.getBean(CustomerService.class);
                if (((CustomerServiceImpl) customerService).login(email, password)) {
                    //==> return Token
                    clientFacade = ((CustomerServiceImpl) customerService);
                    token = tokenManager.addToken(clientFacade);
                    Customer customer = customerService.getTheLoggedCustomerDetails();
                    LoginResponse loginResponse = new LoginResponse();
                    loginResponse.setClientId(customer.getId());
                    loginResponse.setClientEmail(customer.getEmail());
                    loginResponse.setClientName(customer.getFirstName());
                    loginResponse.setClientType(ClientType.CUSTOMER);
                    loginResponse.setClientToken(token);
                    return loginResponse;
                }
                break;
        }
        throw new SecurityException("Invalid email or password");
    }
}
