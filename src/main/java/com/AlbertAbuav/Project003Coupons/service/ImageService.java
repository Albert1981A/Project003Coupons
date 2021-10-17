package com.AlbertAbuav.Project003Coupons.service;

import com.AlbertAbuav.Project003Coupons.beans.Image;
import com.AlbertAbuav.Project003Coupons.exception.invalidImageException;

import java.util.UUID;

public interface ImageService {
    byte[] findImage(UUID uuid) throws invalidImageException;
    boolean isUUIDExist(UUID uuid);
    UUID addImage(byte[] bytes) throws invalidImageException;
    Image getImage(UUID uuid) throws invalidImageException;
}
