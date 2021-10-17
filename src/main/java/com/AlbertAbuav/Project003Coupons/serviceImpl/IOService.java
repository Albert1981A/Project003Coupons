package com.AlbertAbuav.Project003Coupons.serviceImpl;

import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class IOService {

    @Value("${image_folder_path}")
    private String path;

//    @Value("${image_resource_name}")
//    private String imagePrefix;

    public byte[] fromFile(String companyName) throws IOException {
        String fullPath = String.format("%s%s.jpg",path,companyName);
        File file = new File(fullPath);
        byte[] picInBytes = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        int no_bytes_read = fileInputStream.read(picInBytes);
        fileInputStream.close();
        return picInBytes;
    }
}
