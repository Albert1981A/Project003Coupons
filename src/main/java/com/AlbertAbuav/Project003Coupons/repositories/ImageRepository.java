package com.AlbertAbuav.Project003Coupons.repositories;

import com.AlbertAbuav.Project003Coupons.beans.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {

    @Query(value = "SELECT image FROM images where id = ?1",nativeQuery = true)
    byte[] findImageByUUID(UUID uuid);
}
