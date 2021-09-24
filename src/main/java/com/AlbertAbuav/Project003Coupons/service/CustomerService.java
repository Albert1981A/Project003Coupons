package com.AlbertAbuav.Project003Coupons.service;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;

import java.util.List;

public interface CustomerService {

    void addCoupon(Coupon coupon) throws invalidCustomerException;
    List<Coupon> getAllCustomerCoupons() throws invalidCustomerException;
    List<Coupon> getAllCustomerCouponsOfSpecificCategory(Category category) throws invalidCustomerException;
    List<Coupon> getAllCustomerCouponsUpToMaxPrice(double maxPrice) throws invalidCustomerException;
    Customer getTheLoggedCustomerDetails();
    List<Customer> findAllCustomersByCouponId(int couponID);
    Coupon getOneByCouponId(int couponID);

}
