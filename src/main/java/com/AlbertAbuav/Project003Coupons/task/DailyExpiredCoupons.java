package com.AlbertAbuav.Project003Coupons.task;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.repositories.CompanyRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CouponRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CustomerRepository;
import com.AlbertAbuav.Project003Coupons.utils.ChartUtils;
import com.AlbertAbuav.Project003Coupons.utils.Colors;
import com.AlbertAbuav.Project003Coupons.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyExpiredCoupons {

    private final CouponRepository couponRepository;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final ChartUtils chartUtils;

    @Scheduled(fixedRate = 1000*60*60*24)
    public void deleteExpiredCoupons() {
        try {
            Thread.sleep(7 * 1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\"" + Thread.currentThread().getName() + " is checking and deleting expired coupons - " + Colors.GREEN + "RUNNING!...." + Colors.RESET);
        System.out.println();
        List<Coupon> dbExpiredCoupons = couponRepository.findAllByEndDateBefore(DateUtils.javaDateFromLocalDate(LocalDate.now()));
        if (dbExpiredCoupons.size() > 0) {
            for (Coupon coupon : dbExpiredCoupons) {
                List<Customer> couponCustomers = customerRepository.findAllByCoupons_Id(coupon.getId());
                System.out.println("Customers that purchase this coupon id-" + coupon.getId());
                chartUtils.printCustomers(couponCustomers);
                System.out.println();
                List<Coupon> customerCoupons = null;
                for (Customer customer : couponCustomers) {
                    customerCoupons = customer.getCoupons();
                    customerCoupons.removeIf(coupon1 -> coupon1.getId() == coupon.getId());
                    customer.setCoupons(customerCoupons);
                    customerRepository.saveAndFlush(customer);
                    System.out.println("customer coupons after deleting coupon id-" + coupon.getId() + " and updating the data base:");
                    chartUtils.printCustomer(customerRepository.getOne(customer.getId()));
                }
                Company company = companyRepository.getOne(coupon.getCompanyID());
                company.getCoupons().removeIf(coupon1 -> coupon1.getId() == coupon.getId());
                companyRepository.saveAndFlush(company);
                couponRepository.delete(coupon);
                Colors.setYellowBoldPrint("DELETED: | ");
                chartUtils.printCoupon(coupon);
            }
        }
        System.out.println();
        System.out.println("Deleting expired coupons \"" + Thread.currentThread().getName() + "\" is - " + Colors.RED + "STOPPED!....." + Colors.RESET);
    }

}
