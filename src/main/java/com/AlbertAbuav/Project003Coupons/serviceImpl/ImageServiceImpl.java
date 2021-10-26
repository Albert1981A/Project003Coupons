package com.AlbertAbuav.Project003Coupons.serviceImpl;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.beans.Image;
import com.AlbertAbuav.Project003Coupons.controllers.model.CompanyReceiveDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.CustomerReceiveDetails;
import com.AlbertAbuav.Project003Coupons.exception.invalidImageException;
import com.AlbertAbuav.Project003Coupons.repositories.CompanyRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CustomerRepository;
import com.AlbertAbuav.Project003Coupons.repositories.ImageRepository;
import com.AlbertAbuav.Project003Coupons.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;

    @Override
    public byte[] findImage(UUID uuid) throws invalidImageException {
        if (!isUUIDExist(uuid)) {
            throw new invalidImageException("Image Id doesn't exist!");
        }
        return imageRepository.findImageByUUID(uuid);
    }

    @Override
    public boolean isUUIDExist(UUID uuid) {
        return imageRepository.existsById(uuid);
    }

    @Override
    public UUID addImage(byte[] bytes) throws invalidImageException {
        Image image = null;
        UUID uuid = null;
        try {
            image = new Image(bytes);
            uuid = imageRepository.save(image).getId();
        } catch (Exception e) {
            throw new invalidImageException("Image upload failed!");
        }
        return uuid;
    }

    @Override
    public Image getImage(UUID uuid) throws invalidImageException {
        if (!imageRepository.existsById(uuid)) {
            throw new invalidImageException("Image Id wasn't found!");
        }
        return imageRepository.findById(uuid).orElse(new Image());
    }

    @Override
    public Image getImageByCompanyId(int id) throws invalidImageException {
        Company company = companyRepository.getOne(id);
        UUID uuid = company.getImage().getId();
        if (!imageRepository.existsById(uuid)) {
            throw new invalidImageException("Image Id wasn't found!");
        }
        return imageRepository.findById(uuid).orElse(new Image());
    }

    @Override
    public Image getImageByCustomerId(int id) throws invalidImageException {
        Customer customer = customerRepository.getOne(id);
        UUID uuid = customer.getImage().getId();
        if (!imageRepository.existsById(uuid)) {
            throw new invalidImageException("Image Id wasn't found!");
        }
        return imageRepository.findById(uuid).orElse(new Image());
    }

    @Override
    public Company getCompanyFromCompanyReceiveDetails(CompanyReceiveDetails companyReceiveDetails) {
        String name = companyReceiveDetails.getName();
        String email = companyReceiveDetails.getEmail();
        String password = companyReceiveDetails.getPassword();
        UUID uuid = null;
        Image image = null;
        if (companyReceiveDetails.getImage() != null) {
            try {
                uuid = addImage(companyReceiveDetails.getImage().getBytes());
                image = getImage(uuid);
            } catch (invalidImageException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
        String uuids = null;
        if (uuid != null) {
            uuids = uuid.toString();
        }
        Company company = Company.builder()
                .name(name)
                .email(email)
                .password(password)
                .imageID(uuids)
                .image(image)
                .build();
        return company;
    }

    public Customer getCustomerFromCustomerReceiveDetails(CustomerReceiveDetails customerReceiveDetails) {
        String firstName = customerReceiveDetails.getFirstName();
        String lastName = customerReceiveDetails.getLastName();
        String email = customerReceiveDetails.getEmail();
        String password = customerReceiveDetails.getPassword();
        UUID uuid = null;
        Image image = null;
        if (customerReceiveDetails.getImage() != null) {
            try {
                uuid = addImage(customerReceiveDetails.getImage().getBytes());
                image = getImage(uuid);
            } catch (invalidImageException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
        String uuids = null;
        if (uuid != null) {
            uuids = uuid.toString();
        }
        Customer customer = Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .imageID(uuids)
                .image(image)
                .build();
        return customer;
    }
}
