package com.AlbertAbuav.Project003Coupons.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

//@Component
@Data
@RequiredArgsConstructor
public class BASE64DecodedMultipartFile implements MultipartFile {

    private final byte[] fileContent;
    private String fileName;
    private String contentType;
    private File file;
    private String destPath = "C:\\Users\\albrt\\IdeaProjects2\\Project003Coupons\\src\\main\\resources\\static\\";
    private FileOutputStream fileOutputStream;

    public BASE64DecodedMultipartFile(byte[] fileData, String name, String contentType) {
        this.fileContent = fileData;
        this.fileName = name;
        this.contentType = contentType;
        file = new File(destPath + fileName + "1." + contentType);

    }

    @Override
    public String getName() {
        // implementation depends on your requirements
        return fileName;
    }

    @Override
    public String getOriginalFilename() {
        // implementation depends on your requirements
        return fileName;
    }

    @Override
    public String getContentType() {
        // implementation depends on your requirements
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        fileOutputStream = new FileOutputStream(dest);
        fileOutputStream.write(fileContent);
    }

    public void clearOutStreams() throws IOException {
        if (null != fileOutputStream) {
            fileOutputStream.flush();
            fileOutputStream.close();
            file.deleteOnExit();
        }
    }

}
