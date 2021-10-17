package com.AlbertAbuav.Project003Coupons.serviceImpl;

import com.AlbertAbuav.Project003Coupons.beans.Image;
import com.AlbertAbuav.Project003Coupons.exception.invalidImageException;
import com.AlbertAbuav.Project003Coupons.repositories.ImageRepository;
import com.AlbertAbuav.Project003Coupons.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public byte[] findImage(UUID uuid) throws invalidImageException {
        if(!isUUIDExist(uuid)){
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
        if(!imageRepository.existsById(uuid)){
            throw new invalidImageException("Image Id wasn't found!");
        }
        return imageRepository.findById(uuid).orElse(new Image());
    }
}
