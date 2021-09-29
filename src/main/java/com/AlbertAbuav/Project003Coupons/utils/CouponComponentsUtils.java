package com.AlbertAbuav.Project003Coupons.utils;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.repositories.CompanyRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CouponRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponComponentsUtils {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ChartUtils chartUtils;

    //    public void updateCoupon(Coupon coupon) throws invalidCompanyException {
//        if (Objects.isNull(coupon)) {
//            throw new invalidCompanyException("There is no coupon like you entered!");
//        }
//        if (coupon.getCompanyID() != companyID) {
//            throw new invalidCompanyException("The \"company id\" cannot be updated");
//        }
//        Company companyToUpdate = companyRepository.getOne(coupon.getCompanyID());
//        List<Coupon> companyCoupons = companyToUpdate.getCoupons();
//        int count = 0;
//        if (couponRepository.existsByCompanyIDAndTitle(companyID, coupon.getTitle())) {
//            for (Coupon coupon1 : companyCoupons) {
//                if (coupon1.getId() != coupon.getId() && coupon1.getTitle().equals(coupon.getTitle())) {
//                    throw new invalidCompanyException("The \"coupon id\" cannot be updated-1");
//                }
//            }
//            for (Coupon coupon1 : companyCoupons) {
//                if (coupon1.getId() == coupon.getId()) {
//                    companyCoupons.set(count, coupon);
//                    companyToUpdate.setCoupons(companyCoupons);
//                    companyRepository.saveAndFlush(companyToUpdate);
//                    return;
//                }
//                count++;
//            }
//        } else {
//            int count2 = 0;
//            for (Coupon coupon1 : companyCoupons) {
//                if (coupon1.getId() == coupon.getId() && coupon1.getCompanyID() == coupon.getCompanyID()/* && !coupon1.getCategory().equals(coupon.getCategory()) */) {
//                    companyCoupons.set(count2, coupon);
//                    companyToUpdate.setCoupons(companyCoupons);
//                    companyRepository.saveAndFlush(companyToUpdate);
//                    return;
//                }
//                count2++;
//            }
//            throw new invalidCompanyException("The \"coupon id\" cannot be updated-2");
//        }
//    }

    public List<Coupon> updateCouponList(List<Coupon> coupons, Coupon coupon, int companyID) throws invalidCompanyException {
        int count = 0;
        if (couponRepository.existsByCompanyIDAndTitle(companyID, coupon.getTitle())) {
            for (Coupon coupon1 : coupons) {
                if (coupon1.getId() != coupon.getId() && coupon1.getTitle().equals(coupon.getTitle())) {
                    throw new invalidCompanyException("The \"coupon id\" cannot be updated-1");
                }
            }
            for (Coupon coupon1 : coupons) {
                if (coupon1.getId() == coupon.getId()) {
                    coupons.set(count, coupon);
                    break;
                }
                count++;
            }
        } else {
            int count2 = 0;
            for (Coupon coupon1 : coupons) {
                if (coupon1.getId() == coupon.getId() && coupon1.getCompanyID() == coupon.getCompanyID() /* && !coupon1.getCategory().equals(coupon.getCategory()) */) {
                    coupons.set(count2, coupon);
                    break;
                }
                count2++;
            }
//            throw new invalidCompanyException("The \"coupon id\" cannot be updated-2");
        }
        return coupons;
    }

    //    public void deleteCoupon(Coupon coupon) throws invalidCompanyException {
//        if (Objects.isNull(coupon)) {
//            throw new invalidCompanyException("There is no coupon like you entered");
//        }
//        if (!couponRepository.existsByIdAndCompanyID(coupon.getId(), coupon.getCompanyID())) {
//            throw new invalidCompanyException("There is no coupon id-" + coupon.getId() + " in the system!");
//        } else if (coupon.getCompanyID() != companyID) {
//            throw new invalidCompanyException("you can not delete other companies coupons!");
//        }
//        List<Customer> couponCustomers = customerRepository.findAllByCoupons_Id(coupon.getId());
//        System.out.println("Customers that purchase this coupon id-" + coupon.getId());
//        chartUtils.printCustomers(couponCustomers);
//        System.out.println();
//        List<Coupon> customerCoupons = null;
//        for (Customer customer : couponCustomers) {
//            customerCoupons = customer.getCoupons();
//            customerCoupons.removeIf(coupon1 -> coupon1.getId() == coupon.getId());
//            customer.setCoupons(customerCoupons);
//            customerRepository.saveAndFlush(customer);
//            System.out.println("customer coupons after deleting coupon id-" + coupon.getId() + " and updating the data base:");
//            chartUtils.printCustomer(customerRepository.getOne(customer.getId()));
//        }
//        Company company = companyRepository.getOne(coupon.getCompanyID());
//        company.getCoupons().removeIf(coupon1 -> coupon1.getId() == coupon.getId());
//        companyRepository.saveAndFlush(company);
//        couponRepository.delete(coupon);
//        Colors.setYellowBoldPrint("DELETED: | ");
//        chartUtils.printCoupon(coupon);
//    }

    public void deleteSingleCouponFromListOfCustomers(Coupon coupon) {
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
    }

    public void deleteCouponFromCompany(Coupon coupon) {
        Company company = companyRepository.getOne(coupon.getCompanyID());
        company.getCoupons().removeIf(coupon1 -> coupon1.getId() == coupon.getId());
        companyRepository.saveAndFlush(company);
    }

    //                List<Customer> couponCustomers = customerRepository.findAllByCoupons_Id(coupon.getId());
//                System.out.println("Customers that purchase this coupon id-" + coupon.getId());
//                chartUtils.printCustomers(couponCustomers);
//                System.out.println();
//                List<Coupon> customerCoupons = null;
//                for (Customer customer : couponCustomers) {
//                    customerCoupons = customer.getCoupons();
//                    customerCoupons.removeIf(coupon1 -> coupon1.getId() == coupon.getId());
//                    customer.setCoupons(customerCoupons);
//                    customerRepository.saveAndFlush(customer);
//                    System.out.println("customer coupons after deleting coupon id-" + coupon.getId() + " and updating the data base:");
//                    chartUtils.printCustomer(customerRepository.getOne(customer.getId()));
//                }

//                Company company = companyRepository.getOne(coupon.getCompanyID());
//                company.getCoupons().removeIf(coupon1 -> coupon1.getId() == coupon.getId());
//                companyRepository.saveAndFlush(company);
}
