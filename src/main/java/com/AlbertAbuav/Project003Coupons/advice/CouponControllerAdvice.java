package com.AlbertAbuav.Project003Coupons.advice;

import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.exception.SecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class CouponControllerAdvice {

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleException(Exception e) {
        return new ErrorDetails("System exception: ", e.getMessage());
    }

    @ExceptionHandler(value = {invalidAdminException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails adminHandleException(Exception e) {
        return new ErrorDetails("Admin exception: ", e.getMessage());
    }

    @ExceptionHandler(value = {invalidCompanyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails companyHandleException(Exception e) {
        return new ErrorDetails("Company exception: ", e.getMessage());
    }

    @ExceptionHandler(value = {invalidCustomerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails customerHandleException(Exception e) {
        return new ErrorDetails("Customer exception: ", e.getMessage());
    }

    @ExceptionHandler(value = {SecurityException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ErrorDetails securityException(Exception e) {
        return new ErrorDetails("UNAUTHORIZED", e.getMessage());
    }

}
