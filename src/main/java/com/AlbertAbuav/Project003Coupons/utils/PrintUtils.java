package com.AlbertAbuav.Project003Coupons.utils;

import java.util.List;

public class PrintUtils {

    public static String listToString(List<?> list) {
        String result = " ";
        for (int i = 0; i < list.size(); i++) {
            result += "\n   " + list.get(i);
        }
        return result;
    }
}
