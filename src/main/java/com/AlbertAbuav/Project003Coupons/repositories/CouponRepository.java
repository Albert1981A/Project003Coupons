package com.AlbertAbuav.Project003Coupons.repositories;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    List<Coupon> findByCompanyID(int companyID);
    boolean existsByCompanyIDAndCategory(int companyID, Category category);
    List<Coupon> findByCompanyIDAndCategory(int companyID, Category category);
    boolean existsByCompanyIDAndPriceLessThan(int companyID, double maxPrice);
    List<Coupon> findByCompanyIDAndPriceLessThan(int companyID, double maxPrice);
    List<Coupon> findAllByCustomers_Id(int customerID);
    boolean existsByCompanyIDAndTitle(int customerID, String title);
    Coupon findByCompanyIDAndTitle(int customerID, String title);
    boolean existsByIdAndCompanyID(int id, int companyID);
    boolean existsByCustomers_Id(int customerID);
    List<Coupon> findAllByCustomers_IdAndCategory(int customerID, Category category);
    boolean existsByCustomers_IdAndCategory(int customerID, Category category);
    List<Coupon> findAllByCustomers_IdAndPriceLessThan(int customerID, double maxPrice);
    boolean existsByCustomers_IdAndPriceLessThan(int customerID, double maxPrice);
    boolean existsByCustomers_IdAndId(int customerID, int couponID);
    List<Coupon> findAllByEndDateBefore(Date endDate);


    /**
     * 10% Query("...")
     * HQL = Hibernate Query Language
     */
//    @Query(value = "SELECT * FROM `couponsystem002`.`customer_coupons` WHERE (`customer_id` = ?1);", nativeQuery = true)
//    List<CustomerVsCoupons> findAllByCustomer_Id(int customerID);

//    @Query(value = "SELECT * FROM `couponsystem002`.`customer_coupons` WHERE (`coupons_id` = ?1);", nativeQuery = true)
//    List<CustomerVsCoupons> getAllCustomersOfASingleCouponByCouponId(int couponID);

}
