package com.AlbertAbuav.Project003Coupons.exception;

import com.AlbertAbuav.Project003Coupons.utils.Colors;

public class invalidImageException extends Exception {
    public invalidImageException(String message) {
        super(Colors.RED + "This is an invalid Image operation: " + message + Colors.RESET);
    }
}
