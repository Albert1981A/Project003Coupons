package com.AlbertAbuav.Project003Coupons.login;

import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import com.AlbertAbuav.Project003Coupons.serviceImpl.AdminServiceImpl;
import com.AlbertAbuav.Project003Coupons.serviceImpl.CompanyServiceImpl;
import com.AlbertAbuav.Project003Coupons.serviceImpl.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
@RequiredArgsConstructor
public class LoginManager {

    private final ApplicationContext ctx;
    private final TokenManager tokenManager;

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
}
