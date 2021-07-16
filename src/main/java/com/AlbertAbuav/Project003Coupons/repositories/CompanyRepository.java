package com.AlbertAbuav.Project003Coupons.repositories;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    boolean existsByEmailAndPassword(String email, String password);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    boolean existsById(int id);
    Company findByEmailAndPassword(String email, String password);
    Company findByName(String name);
}
