package com.AlbertAbuav.Project003Coupons.exception;

import com.AlbertAbuav.Project003Coupons.utils.Colors;

public class invalidCustomerException extends Exception {
    public invalidCustomerException(String message) {
        super(Colors.RED + "This is an invalid Customer operation: " + message + Colors.RESET);
    }
}
