package com.AlbertAbuav.Project003Coupons.service;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.beans.Image;
import com.AlbertAbuav.Project003Coupons.controllers.model.CompanyReceiveDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.CustomerReceiveDetails;
import com.AlbertAbuav.Project003Coupons.exception.invalidImageException;

import java.util.UUID;

public interface ImageService {
    byte[] findImage(UUID uuid) throws invalidImageException;
    boolean isUUIDExist(UUID uuid);
    UUID addImage(byte[] bytes) throws invalidImageException;
    Image getImage(UUID uuid) throws invalidImageException;
    Image getImageByCompanyId(int id) throws invalidImageException;
    Image getImageByCustomerId(int id) throws invalidImageException;
    Company getCompanyFromCompanyReceiveDetails(CompanyReceiveDetails companyReceiveDetails);
    Customer getCustomerFromCustomerReceiveDetails(CustomerReceiveDetails customerReceiveDetails);
}
