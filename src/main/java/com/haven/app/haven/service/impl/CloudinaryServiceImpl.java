package com.haven.app.haven.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.haven.app.haven.service.CloudinaryService;
import com.haven.app.haven.utils.FileValidator;
import com.haven.app.haven.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;
    @Override
    public String uploadImage(MultipartFile file, String userId) throws IOException {
        try {
            FileValidator.validateImage(file);
            Map<String, Object> options = new HashMap<>();
            options.put("public_id", userId);
            options.put("folder", "haven/user-images");
            options.put("resource_type", "auto");
            options.put("max_bytes", 2097152);

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            LogUtils.logSuccess("CloudinaryService", "uploadImage");

            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            LogUtils.getError("CloudinaryService.uploadImage", e);
            throw e;
        }
    }

    @Override
    public void deleteImage(String userId) throws IOException {
        try {
            Map<String, Object> options = new HashMap<>();
            cloudinary.uploader().destroy(userId, options);
            LogUtils.logSuccess("CloudinaryService", "deleteImage");
        } catch (Exception e) {
            LogUtils.getError("CloudinaryService.deleteImage", e);
            throw e;
        }
    }
}
