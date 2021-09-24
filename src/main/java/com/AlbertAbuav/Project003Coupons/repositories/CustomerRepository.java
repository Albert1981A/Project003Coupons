package com.AlbertAbuav.Project003Coupons.repositories;

import com.AlbertAbuav.Project003Coupons.beans.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    boolean existsById(int id);
    Customer findByEmailAndPassword(String email, String password);
    Customer findByEmail(String email);
    List<Customer> findAllByCoupons_Id(int couponID);
}
