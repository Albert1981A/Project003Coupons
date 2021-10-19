package com.AlbertAbuav.Project003Coupons.serviceImpl;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.exception.invalidImageException;
import com.AlbertAbuav.Project003Coupons.repositories.CompanyRepository;
import com.AlbertAbuav.Project003Coupons.repositories.ImageRepository;
import com.AlbertAbuav.Project003Coupons.service.AdminService;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class IOService {

    @Value("${image_folder_path}")
    private String path;
    private final CompanyRepository companyRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

//    @Value("${image_resource_name}")
//    private String imagePrefix;

    public byte[] fromFile(String companyName) throws IOException {
        Company company = null;
        if (companyRepository.existsByName(companyName)) {
            company = companyRepository.findByName(companyName);
        }

        byte[] picInBytes = null;
        if (company == null) {
            String fullPath = String.format("%s%s.jpg", path, companyName);
            System.out.println(fullPath);
            File file = new File(fullPath);
            picInBytes = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            int no_bytes_read = fileInputStream.read(picInBytes);
            fileInputStream.close();
        } else {
            try {
                System.out.println("hii... Im Here !!!");
                picInBytes = imageService.findImage(company.getImage().getId());
            } catch (invalidImageException e) {
                System.out.println(e.getMessage());
            }
        }
        return picInBytes;
    }
}
