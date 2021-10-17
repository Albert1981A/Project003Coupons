package com.AlbertAbuav.Project003Coupons.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class BASE64DecodedMultipartFile implements MultipartFile {

    private final String name;
    private final String originalFilename;
    private final String contentType;
    private final byte[] imgContent;

    public BASE64DecodedMultipartFile(String name, String originalFilename, String contentType, byte[] imgContent) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.imgContent = imgContent;
    }

    @Override
    public String getName() {
        // implementation depends on your requirements
        return name;
    }

    @Override
    public String getOriginalFilename() {
        // implementation depends on your requirements
        return originalFilename;
    }

    @Override
    public String getContentType() {
        // implementation depends on your requirements
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
}
