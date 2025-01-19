package com.haven.app.haven.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadImage(MultipartFile file, String userId) throws IOException;

    void deleteImage(String userId) throws IOException;
}
