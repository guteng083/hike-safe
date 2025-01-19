package com.haven.app.haven.utils;

import org.springframework.web.multipart.MultipartFile;

public class FileValidator {
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB in byte
    private static final String[] ALLOWED_CONTENT_TYPES = {
            "image/jpeg",
            "image/png",
            "image/jpg"
    };

    public static void validateImage(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 2MB");
        }

        String contentType = file.getContentType();
        boolean isValidContentType = false;
        for (String allowedType : ALLOWED_CONTENT_TYPES) {
            if (allowedType.equals(contentType)) {
                isValidContentType = true;
                break;
            }
        }

        if (!isValidContentType) {
            throw new IllegalArgumentException("Invalid file type. Only JPEG, JPG, and PNG are allowed");
        }
    }
}
