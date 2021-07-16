package com.AlbertAbuav.Project003Coupons.service;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;

import java.util.List;

public interface CompanyService {

    void addCoupon(Coupon coupon) throws invalidCompanyException;
    void updateCoupon(Coupon coupon) throws invalidCompanyException;
    void deleteCoupon(Coupon coupon) throws invalidCompanyException;
    List<Coupon> getAllCompanyCoupons();
    List<Coupon> getAllCompanyCouponsOfSpecificCategory(Category category) throws invalidCompanyException;
    List<Coupon> getAllCompanyCouponsUpToMaxPrice(double maxPrice) throws invalidCompanyException;
    Company getTheLoggedCompanyDetails();
    Coupon getSingleCoupon(int id) throws invalidCompanyException;
    List<Customer> getAllCompanyCustomersOfASingleCouponByCouponId(int couponID) throws invalidCompanyException;

}
