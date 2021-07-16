package com.AlbertAbuav.Project003Coupons.service;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;

import java.util.List;

public interface AdminService {

    void addCompany(Company company) throws invalidAdminException;
    void updateCompany(Company company) throws invalidAdminException;
    void deleteCompany(Company company) throws invalidAdminException;
    List<Company> getAllCompanies() throws invalidAdminException;
    Company getSingleCompany(int id) throws invalidAdminException;
    void addCustomer(Customer customer) throws invalidAdminException;
    void updateCustomer(Customer customer) throws invalidAdminException;
    void deleteCustomer(Customer customer) throws invalidAdminException;
    List<Customer> getAllCustomers() throws invalidAdminException;
    Customer getSingleCustomer(int id) throws invalidAdminException;

}

