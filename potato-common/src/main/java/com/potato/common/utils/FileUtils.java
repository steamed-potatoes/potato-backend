package com.potato.common.utils;

import com.potato.common.exception.model.ValidationException;
import com.potato.common.type.ImageType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.potato.common.exception.ErrorCode.VALIDATION_FILE_FORMAT_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    private static final List<String> imageContentTypes = Arrays.asList("image/jpeg", "image/png");

    public static String createFileUuidNameWithExtension(ImageType type, String originalFileName) {
        return type.getFileNameWithDirectory(UUID.randomUUID().toString().concat(getFileExtension(originalFileName)));
    }

    private static String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ValidationException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName), VALIDATION_FILE_FORMAT_EXCEPTION);
        }
    }

    public static void validateImageFile(String contentType) {
        if (!imageContentTypes.contains(contentType)) {
            throw new ValidationException(String.format("허용되지 않은 파일 형식 (%s) 입니다", contentType), VALIDATION_FILE_FORMAT_EXCEPTION);
        }
    }

}
