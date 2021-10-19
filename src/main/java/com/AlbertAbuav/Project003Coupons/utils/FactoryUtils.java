package com.AlbertAbuav.Project003Coupons.utils;

import com.AlbertAbuav.Project003Coupons.beans.*;
import com.AlbertAbuav.Project003Coupons.controllers.model.CompanyReceiveDetails;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidImageException;
import com.AlbertAbuav.Project003Coupons.repositories.CompanyRepository;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.ImageService;
import com.AlbertAbuav.Project003Coupons.serviceImpl.IOService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FactoryUtils {

    private static int COUNT1 = 1;
    private static int COUNT2 = 1;

    private final CompanyRepository companyRepository;

    private BASE64DecodedMultipartFile multipartFile;
    private final IOService ioService;
    private final AdminService adminService;
    private final ImageService imageService;

    /**
     * This method generate a random customer first name.
     *
     * @return String
     */
    public String generateFirstName() {
        FirstName firstName = FirstName.values()[(int) (Math.random() * FirstName.values().length)];
        return firstName.toString();
    }

    /**
     * This method generate a random customer last name.
     *
     * @return String
     */
    public String generateLastName() {
        LastName lastName = LastName.values()[(int) (Math.random() * LastName.values().length)];
        return lastName.toString();
    }

    /**
     * This method sends a random company name.
     * There can be no two identical company names!
     * The method checks in the data base if the name exists before sending it.
     * If the name exist it's changes.
     *
     * @return String
     */
    public String generateCompanyName() {
        String companyName = CompaniesType.values()[(int) (Math.random() * CompaniesType.values().length)].toString();
        while (companyRepository.existsByName(companyName)) {
            companyName = CompaniesType.values()[(int) (Math.random() * CompaniesType.values().length)].toString();
        }
        return companyName;
    }

    /**
     * This method generate a random ending for a customers mail.
     *
     * @return String
     */
    public String generateCustomerEmailType() {
        EmailType emailType = EmailType.values()[(int) (Math.random() * EmailType.values().length)];
        return "@" + emailType.toString() + ".com";
    }

    /**
     * This method generate a random 8 figure password.
     *
     * @return String
     */
    public String createPassword() {
        String name = UUID.randomUUID().toString();
        char[] nameChar = name.toCharArray();
        char[] password = new char[8];
        for (int i = 0; i < password.length; i++) {
            password[i] = nameChar[i];
        }
        return String.valueOf(password);
    }


    public Customer createCustomer() {
        String firstName = generateFirstName();
        String email = createEmail(firstName);
        return Customer.builder()
                .firstName(firstName)
                .lastName(generateLastName())
                .email(email)
                .password(createPassword())
                .build();
    }

    public String createEmail(String firstName) {
        String email = firstName + (COUNT1++) + generateCustomerEmailType();
        return email.toLowerCase(Locale.ROOT);
    }

    public CompanyReceiveDetails createCompanyReceiveDetails() throws IOException {
        String name = generateCompanyName();
        String email = createCompanyEmail(name);
        Image image = new Image(ioService.fromFile(name));
        UUID uuid = null;
        try {
            uuid = imageService.addImage(image.getImage());
        } catch (invalidImageException e) {
            System.out.println(e.getMessage());
        }
//        Company company = createCompany();
//        try {
//            adminService.addCompany(company);
//        } catch (invalidAdminException e) {
//            System.out.println(e.getMessage());
//        }
        multipartFile = new BASE64DecodedMultipartFile(image.getImage(), name, "jpg");
        CompanyReceiveDetails companyReceiveDetails = null;
        try {
            multipartFile.transferTo(multipartFile.getFile());
            companyReceiveDetails = CompanyReceiveDetails.builder()
                    .name(name)
                    .email(email)
                    .password(createPassword())
                    .imageID(uuid.toString())
                    .image(multipartFile)
                    .build();
        } catch (IllegalStateException e) {
            System.out.println("IllegalStateException : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException : " + e.getMessage());
        }
        return companyReceiveDetails;
    }

    public Company createCompany() throws IOException {
        String name = generateCompanyName();
        String email = createCompanyEmail(name);
        Image image = new Image(ioService.fromFile(name));
        return Company.builder()
                .name(name)
                .email(email)
                .password(createPassword())
                .image(image)
                .build();
    }

    public String createCompanyEmail(String name) {
        String email = generateFirstName() + "@" + name + ".com";
        return email.toLowerCase(Locale.ROOT);
    }

    public Coupon createCoupon() {
        return Coupon.builder()
                .companyID(COUNT2)
                .category(Category.values()[(int) (Math.random() * Category.values().length)])
                .title("Title: " + COUNT2)
                .description("Description: " + COUNT2)
                .startDate(DateUtils.javaDateFromLocalDate(LocalDate.now().minusDays(3)))
                .endDate(DateUtils.javaDateFromLocalDate(LocalDate.now().plusDays(7)))
                .amount((int) (Math.random() * 21) + 30)
                .price((int) (Math.random() * 41) + 60)
                .image("Image" + COUNT2++ + ".jpg")
                .build();
    }

    public Coupon createCouponOfACompany(int companyID) {
        return Coupon.builder()
                .companyID(companyID)
                .category(Category.values()[(int) (Math.random() * Category.values().length)])
                .title("Title: " + COUNT2)
                .description("Description: " + COUNT2++)
                .startDate(DateUtils.javaDateFromLocalDate(LocalDate.now().minusDays(3)))
                .endDate(DateUtils.javaDateFromLocalDate(LocalDate.now().plusDays(7)))
                .amount((int) (Math.random() * 21) + 30)
                .price((int) (Math.random() * 41) + 60)
                .image("Image" + companyID + ".jpg")
                .build();
    }


}
