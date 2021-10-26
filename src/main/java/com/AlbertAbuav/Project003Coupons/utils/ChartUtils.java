package com.AlbertAbuav.Project003Coupons.utils;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.repositories.CouponRepository;
import com.AlbertAbuav.Project003Coupons.wrappers.ListOfCoupons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class ChartUtils {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CouponRepository customerRepository;

    private final String DATE_FORMAT = "dd/MM/yyyy";
    private final DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

    private final String COMPANY_HEADER = String.format("%s %3s %s %15s %s %30s %s %15s %s %50s %s", "||", "Id", "|", centerString("Name", 15), "|", centerString("Email", 30), "|" , centerString("Password", 15), "|" , centerString("Image", 50) , "||");
    private final String COMPANY_THIN_SEPARATION_LINE = String.format("%s", "-----------------------------------------------------------------------------------------------------------------------------------");
    private final String COMPANY_THICK_SEPARATION_LINE = String.format("%s", "===================================================================================================================================");
    private final String CUSTOMER_HEADER = String.format("%s %3s %s %20s %s %20s %s %30s %s %15s %s %50s %s", "||", "Id", "|", centerString("First name", 20), "|", centerString("Last name", 20), "|", centerString("Email", 30), "|" , centerString("Password", 15), "|" , centerString("Image", 50) , "||");
    private final String CUSTOMER_THIN_SEPARATION_LINE = String.format("%s", "---------------------------------------------------------------------------------------------------------------------------------------------------------------");
    private final String CUSTOMER_THICK_SEPARATION_LINE = String.format("%s", "===============================================================================================================================================================");
    private final String COUPON_HEADER = String.format("%s %3s %s %12s %s %40s %s %20s %s %20s %s %15s %s %15s %s %10s %s %10s %s %15s %s", "||", "Id", "|", centerString("Company Id", 12), "|", centerString("Category", 40), "|", centerString("Title", 20), "|", centerString("Description", 20), "|", centerString("Start Date", 15), "|", centerString("End Date", 15), "|", centerString("Amount", 10), "|", centerString("Price", 10), "|", centerString("Image", 25), "||");
    private final String COUPON_THIN_SEPARATION_LINE = String.format("%s", "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    private final String COUPON_THICK_SEPARATION_LINE = String.format("%s", "===========================================================================================================================================================================================================");
    private final String COUPON_TWO_VERTICAL = Colors.YELLOW_BOLD + "||" + Colors.RESET;

    public String centerString(String str, int len) {
        int padSize = len - str.length();
        int padStart = padSize / 2;
        int padEnd;
        if (padSize%2 == 0) {
            padEnd = (padSize / 2);
        } else {
            padEnd = (padSize / 2) + 1;
        }
        return String.format("%" + padStart + "s" + "%" + str.length() + "s" + "%" +padEnd+ "s", "", str, "");
    }

    public void printCompanyHeader() {
        Colors.setCyanBoldPrint(COMPANY_THICK_SEPARATION_LINE);
        Colors.setCyanBoldPrint(COMPANY_HEADER);
        Colors.setCyanBoldPrint(COMPANY_THICK_SEPARATION_LINE);
    }

    public void printCompanyDetails(Company c) {
        String imageDetails;
        if (c.getImage().getId() == null) {
            imageDetails = "null";
        } else {
            imageDetails = c.getImage().getId().toString();
        }
        String COMPANY_VERTICAL = Colors.CYAN_BOLD + "|" + Colors.RESET;
        String COMPANY_TWO_VERTICAL = Colors.CYAN_BOLD + "||" + Colors.RESET;
        System.out.printf("%s %3d %s %15s %s %30s %s %15s %s %50s %s%n", COMPANY_TWO_VERTICAL, c.getId(), COMPANY_VERTICAL, centerString(c.getName(),15), COMPANY_VERTICAL, centerString(c.getEmail(), 30), COMPANY_VERTICAL, centerString(c.getPassword(), 15), COMPANY_VERTICAL, centerString(imageDetails, 50), COMPANY_TWO_VERTICAL);
        Colors.setCyanBoldPrint(COMPANY_THIN_SEPARATION_LINE);
        //System.out.printf("%10s %77s %s%n", Colors.CYAN_BOLD + "|| Coupons:" + Colors.RESET, Colors.CYAN_BOLD + "||" + Colors.RESET, PrintUtils.listToString(c.getCoupons()));
        printCoupons(c.getCoupons());
        Colors.setCyanBoldPrint(COMPANY_THIN_SEPARATION_LINE);
    }

    public void printCompany (Company company) {
        printCompanyHeader();
        printCompanyDetails(company);
    }

    public void printCompanies (List<Company> companies) {
        printCompanyHeader();
        for (Company company : companies) {
            List <Coupon> temp = couponRepository.findByCompanyID(company.getId());
            if (temp.size() != 0) {
                company.setCoupons(temp);
            }
            printCompanyDetails(company);
        }
    }


//==========================================================


    public void printCustomerHeader() {
        Colors.setPurpleBoldPrint(CUSTOMER_THICK_SEPARATION_LINE);
        Colors.setPurpleBoldPrint(CUSTOMER_HEADER);
        Colors.setPurpleBoldPrint(CUSTOMER_THICK_SEPARATION_LINE);
    }

    public void printCustomerDetails(Customer cu) {
        String imageDetails;
        if (cu.getImage().getId() == null) {
            imageDetails = "null";
        } else {
            imageDetails = cu.getImage().getId().toString();
        }
        String CUSTOMER_VERTICAL = Colors.PURPLE_BOLD + "|" + Colors.RESET;
        String CUSTOMER_TWO_VERTICAL = Colors.PURPLE_BOLD + "||" + Colors.RESET;
        System.out.printf("%s %3d %s %20s %s %20s %s %30s %s %15s %s %50s %s%n", CUSTOMER_TWO_VERTICAL, cu.getId(), CUSTOMER_VERTICAL, centerString(cu.getFirstName(),20), CUSTOMER_VERTICAL, centerString(cu.getLastName(), 20), CUSTOMER_VERTICAL, centerString(cu.getEmail(), 30), CUSTOMER_VERTICAL, centerString(cu.getPassword(), 15), CUSTOMER_VERTICAL, centerString(imageDetails, 50), CUSTOMER_TWO_VERTICAL);
        Colors.setPurpleBoldPrint(CUSTOMER_THIN_SEPARATION_LINE);
        //System.out.printf("%10s %105s %s%n", Colors.PURPLE_BOLD + "|| Coupons:" + Colors.RESET, Colors.PURPLE_BOLD + "||" + Colors.RESET, PrintUtils.listToString(cu.getCoupons()));
        printCoupons(cu.getCoupons());
        Colors.setPurpleBoldPrint(CUSTOMER_THIN_SEPARATION_LINE);
    }

    public void printCustomer (Customer customer) {
        printCustomerHeader();
        printCustomerDetails(customer);
    }

    public void printCustomers (List<Customer> customers) {
        printCustomerHeader();
        for (Customer customer : customers) {
            printCustomerDetails(customer);
        }
    }

//==========================================================


    public void printCouponHeader() {
        Colors.setYellowBoldPrint(COUPON_THICK_SEPARATION_LINE);
        Colors.setYellowBoldPrint(COUPON_HEADER);
        Colors.setYellowBoldPrint(COUPON_THICK_SEPARATION_LINE);
    }

    public void printCouponDetails(Coupon co) {
        String COUPON_VERTICAL = Colors.YELLOW_BOLD + "|" + Colors.RESET;
        System.out.printf("%s %3d %s %12s %s %40s %s %20s %s %20s %s %15s %s %15s %s %10s %s %10s %s %15s %s%n", COUPON_TWO_VERTICAL, co.getId(), COUPON_VERTICAL, centerString(String.valueOf(co.getCompanyID()), 12), COUPON_VERTICAL, centerString(co.getCategory().toString(), 40), COUPON_VERTICAL, centerString(co.getTitle(),20), COUPON_VERTICAL, centerString(co.getDescription(), 20), COUPON_VERTICAL, centerString(formatter.format(co.getStartDate()), 15), COUPON_VERTICAL, centerString(formatter.format(co.getEndDate()), 15), COUPON_VERTICAL, centerString(String.valueOf(co.getAmount()), 10), COUPON_VERTICAL, centerString(String.valueOf(co.getPrice()), 10), COUPON_VERTICAL, centerString(co.getImage(),25), COUPON_TWO_VERTICAL);
        Colors.setYellowBoldPrint(COUPON_THIN_SEPARATION_LINE);
    }

    public void printCoupon (Coupon coupon) {
        printCouponHeader();
        printCouponDetails(coupon);
    }

    public void printCoupons (List<Coupon> coupons) {
        if (coupons.size() == 0) {
            System.out.printf("%s %20s%n", COUPON_TWO_VERTICAL, Colors.RED_BOLD + "NO COUPONS" + Colors.RESET);
        } else {
            printCouponHeader();
            for (Coupon coupon : coupons) {
                printCouponDetails(coupon);
            }
        }
    }

}
