package com.potato.api.service.upload;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.potato.api.service.upload.request.FileUploadRequest;
import com.potato.common.exception.model.BadGatewayException;
import com.potato.common.exception.model.ValidationException;
import com.potato.common.type.ImageType;
import com.potato.external.external.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.potato.common.exception.ErrorCode.VALIDATION_FILE_FORMAT_EXCEPTION;

@RequiredArgsConstructor
@Service
public class FileUploadService {

    private static final List<String> imageContentTypes = Arrays.asList("image/jpeg", "image/png");

    private final S3Service s3Service;

    public String uploadImageToStorage(FileUploadRequest request, MultipartFile file) {
        validateFileType(file.getContentType());
        String fileName = createFileName(request.getType(), file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new BadGatewayException(String.format("파일 (%s) 입력 스트림을 가져오는 중 에러가 발생하였습니다", file.getOriginalFilename()));
        }
        return s3Service.getFileUrl(fileName);
    }

    private void validateFileType(String contentType) {
        if (!imageContentTypes.contains(contentType)) {
            throw new ValidationException(String.format("허용되지 않은 파일 형식 (%s) 입니다", contentType), VALIDATION_FILE_FORMAT_EXCEPTION);
        }
    }

    private String createFileName(ImageType type, String originalFileName) {
        return type.getFileNameWithDirectory(UUID.randomUUID().toString().concat(getFileExtension(originalFileName)));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ValidationException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName), VALIDATION_FILE_FORMAT_EXCEPTION);
        }
    }

}
