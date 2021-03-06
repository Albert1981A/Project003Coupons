package com.AlbertAbuav.Project003Coupons.controllers;

import com.AlbertAbuav.Project003Coupons.aop.MyUpdateToken;
import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.beans.Image;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginResponse;
import com.AlbertAbuav.Project003Coupons.controllers.model.LogoutDetails;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.exception.invalidImageException;
import com.AlbertAbuav.Project003Coupons.login.ClientType;
import com.AlbertAbuav.Project003Coupons.login.LoginManager;
import com.AlbertAbuav.Project003Coupons.repositories.CompanyRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CouponRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CustomerRepository;
import com.AlbertAbuav.Project003Coupons.security.Information;
import com.AlbertAbuav.Project003Coupons.security.TokenManager;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import com.AlbertAbuav.Project003Coupons.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("customer-service")  //==>  http://localhost:8080/customer-service
@RequiredArgsConstructor
public class CustomerController {

    private CustomerService customerService;
    private final ImageService imageService;
    private final TokenManager tokenManager;

//    @PostMapping("login") // ==>  http://localhost:8080/customer-service/login
//    @Override
//    public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails) throws invalidCompanyException, invalidAdminException, invalidCustomerException {
//        String token = loginManager.login(loginDetails.getEmail(), loginDetails.getPassword(), ClientType.CUSTOMER);
//        Information information = tokenManager.getMap().get(token);
//        customerService = (CustomerService) information.getClientFacade();
//        return new ResponseEntity<>(token, HttpStatus.CREATED);  //==> return String token + 201
//    }
//
//    @DeleteMapping("logout") // ==>  http://localhost:8080/customer-service/logout
//    @Override
//    public ResponseEntity<?> logOut(@RequestHeader(value = "Authorization") String token, @RequestBody LogoutDetails logoutDetails) {
//        tokenManager.deleteToken(logoutDetails.getToken());
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  //==> Return 204 (ok and no content)
//    }

    public CustomerService getCustomerService(String token) {
        Information information = tokenManager.getMap().get(token);
        return (CustomerService) information.getClientFacade();
    }

    @MyUpdateToken //update the token time to current time
    @PostMapping("coupons") // ==>  http://localhost:8080/customer-service/coupons
    public ResponseEntity<?> addCoupon(@RequestHeader(value = "Authorization") String token, @RequestBody Coupon coupon) throws invalidCustomerException {
//        tokenManager.updateToken(token); //update the token time to current time
        customerService = getCustomerService(token);
        customerService.addCoupon(coupon);
        List<Coupon> coupons = customerService.getTheLoggedCustomerDetails().getCoupons();
        Coupon added = null;
        for (Coupon coupon1 : coupons) {
            if (coupon.getId() == coupon1.getId()) {
                added = coupon1;
            }
        }
        return new ResponseEntity<>(added, HttpStatus.CREATED);  //==> Return 201 (created)
    }

    @MyUpdateToken //update the token time to current time
    @GetMapping("coupons")  //==>  http://localhost:8080/customer-service/coupons
    public ResponseEntity<?> getAllCustomerCoupons(@RequestHeader(value = "Authorization") String token) throws invalidCustomerException {
        customerService = getCustomerService(token);
        return new ResponseEntity<>(customerService.getAllCustomerCoupons(), HttpStatus.OK); //==> Return body + 200
    }

    @MyUpdateToken //update the token time to current time
    @GetMapping("coupons/category")  //==>  http://localhost:8080/customer-service/coupons/category/?category=1  (category=1)
    public ResponseEntity<?> getAllCustomerCouponsOfSpecificCategory(@RequestHeader(value = "Authorization") String token, @RequestParam("category") Category category) throws invalidCustomerException {
        customerService = getCustomerService(token);
        return new ResponseEntity<>(customerService.getAllCustomerCouponsOfSpecificCategory(category), HttpStatus.OK); //==> Return body + 200
    }

    @MyUpdateToken //update the token time to current time
    @GetMapping("coupons/max-price")  //==>  http://localhost:8080/customer-service/coupons/max-price/?max-price=80.2  (max-price=80.2)
    public ResponseEntity<?> getAllCustomerCouponsUpToMaxPrice(@RequestHeader(value = "Authorization") String token, @RequestParam(value = "max-price") double maxPrice) throws invalidCustomerException {
        customerService = getCustomerService(token);
        return new ResponseEntity<>(customerService.getAllCustomerCouponsUpToMaxPrice(maxPrice), HttpStatus.OK); //==> Return body + 200
    }

    @MyUpdateToken //update the token time to current time
    @GetMapping("customer-details")  //==>  http://localhost:8080/customer-service/customer-details
    public ResponseEntity<?>  getTheLoggedCustomerDetails(@RequestHeader(value = "Authorization") String token) {
        customerService = getCustomerService(token);
        return new ResponseEntity<>(customerService.getTheLoggedCustomerDetails(), HttpStatus.OK); //==> Return body + 200
    }

    @MyUpdateToken //update the token time to current time
    @GetMapping("customers-by-coupon-id/{id}")  //==>  http://localhost:8080/customer-service/customers-by-coupon-id/1 (id=1)
    public ResponseEntity<?>  findAllCustomersByCouponId(@RequestHeader(value = "Authorization") String token, @PathVariable int id) {
        customerService = getCustomerService(token);
        return new ResponseEntity<>(customerService.findAllCustomersByCouponId(id), HttpStatus.OK); //==> Return body + 200
    }

    @RequestMapping(value = "/images/customer-id/{id}", method = RequestMethod.GET) //==>  http://localhost:8080/customer-service/images/customer-id/1
    public ResponseEntity<?> getImageByCustomerId(@PathVariable int id, HttpServletResponse response) throws IOException, invalidImageException {

        Image image = imageService.getImageByCustomerId(id);

        response.setHeader("Content-Disposition", "inline;filename=\"" + image.getId().toString() + "\"");
        OutputStream out = response.getOutputStream();
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(new ByteArrayInputStream(image.getImage()), out);
        out.close();

        return null;
    }
}
