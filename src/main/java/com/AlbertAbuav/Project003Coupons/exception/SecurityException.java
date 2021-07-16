package com.AlbertAbuav.Project003Coupons.exception;

import com.AlbertAbuav.Project003Coupons.utils.Colors;

public class SecurityException extends Exception{
    public SecurityException(String message) {
        super(Colors.RED + "This is a security: " + message + Colors.RESET);
    }
}
