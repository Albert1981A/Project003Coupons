package com.AlbertAbuav.Project003Coupons.utils;

public class TestUtils {

    private static int COUNT1 = 1;
    private static int COUNT2 = 1;
    private static int COUNT3 = 1;

    public static void testAdminInfo(String content) {
        System.out.println();
        Colors.setGreenBoldPrint("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        Colors.setGreenBoldPrint(String.format("|>                      TEST ADMIN - %d : %s", COUNT1, content));
        Colors.setGreenBoldPrint("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        COUNT1++;
    }

    public static void testCompanyInfo(String content) {
        System.out.println();
        Colors.setCyanBoldPrint("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        Colors.setCyanBoldPrint(String.format("|>                       TEST COMPANY - %d : %s", COUNT2, content));
        Colors.setCyanBoldPrint("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        COUNT2++;
    }

    public static void testCustomerInfo(String content) {
        System.out.println();
        Colors.setPurpleBoldPrint("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        Colors.setPurpleBoldPrint(String.format("|>                     TEST CUSTOMER - %d : %s", COUNT3, content));
        Colors.setPurpleBoldPrint("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        COUNT3++;
    }
}
