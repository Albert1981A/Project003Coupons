package com.AlbertAbuav.Project003Coupons.advice;

import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.exception.SecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@ControllerAdvice
public class CouponControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleException(Exception e) {
        return new ErrorDetails("System exception: ", e.getMessage());
    }

    @ExceptionHandler(invalidAdminException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails adminHandleException(Exception e) {
        return new ErrorDetails("Admin exception: ", e.getMessage());
    }

    @ExceptionHandler(invalidCompanyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails companyHandleException(invalidCompanyException e) {
        return new ErrorDetails("Company exception: ", e.getMessage());
    }

    @ExceptionHandler(invalidCustomerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails customerHandleException(Exception e) {
        return new ErrorDetails("Customer exception: ", e.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails HttpClientErrorException1(HttpClientErrorException e) {
        return new ErrorDetails("Controller exception1: ", e.getMessage());
    }
    //IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails IllegalStateException1(IllegalStateException e) {
        return new ErrorDetails("Controller exception2: ", e.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ErrorDetails securityException(Exception e) {
        return new ErrorDetails("UNAUTHORIZED", e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> validationErrorHandler(ConstraintViolationException e) {
        List<String> errors = new ArrayList<>(e.getConstraintViolations().size());

        e.getConstraintViolations().forEach(constraintViolation -> {
            errors.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
