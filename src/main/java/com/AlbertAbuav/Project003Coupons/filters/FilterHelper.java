package com.AlbertAbuav.Project003Coupons.filters;

import com.AlbertAbuav.Project003Coupons.exception.SecurityException;

public class FilterHelper {

    public static String getApi(String url) throws SecurityException {
        try{
            String[] step1 = url.split("/api");
            String[] step2 = step1[1].split("/");
            return step2[1].toLowerCase();
        }
        catch (Exception e){
            throw new SecurityException("Unable to locate API name");
        }
    }
}
