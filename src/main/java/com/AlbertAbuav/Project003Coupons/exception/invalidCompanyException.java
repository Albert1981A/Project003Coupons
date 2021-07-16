package com.AlbertAbuav.Project003Coupons.exception;

import com.AlbertAbuav.Project003Coupons.utils.Colors;

public class invalidCompanyException extends Exception {
    public invalidCompanyException(String message) {
        super(Colors.RED + "This is an invalid Company operation: " + message + Colors.RESET);
    }
}
