package com.haven.app.haven.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class LogUtils {
    public static void getError(String context, Exception e) {
        log.error("[{}] Error occurred - Type: {} - Message: {}",
                context,
                e.getClass().getSimpleName(),
                e.getMessage());

        if (e.getCause() != null) {
            log.error("[{}] Caused by: {}", context, e.getCause().getMessage());
        }

        log.error("[{}] Stack trace: {}",
                context,
                Arrays.toString(e.getStackTrace())
                        .replace(",", System.lineSeparator())
        );
    }

    public static void logSuccess(String service, String method) {
        log.info("[{}.{}] Operation completed successfully", service, method);
    }
}
