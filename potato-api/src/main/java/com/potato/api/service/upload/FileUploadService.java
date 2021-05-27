package com.potato.api.service.upload;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.potato.api.service.upload.request.FileUploadRequest;
import com.potato.common.exception.model.BadGatewayException;
import com.potato.common.utils.FileUtils;
import com.potato.external.external.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class FileUploadService {

    private final S3Service s3Service;

    public String uploadImageToStorage(FileUploadRequest request, MultipartFile file) {
        FileUtils.validateImageFile(file.getContentType());
        final String fileName = FileUtils.createFileUuidNameWithExtension(request.getType(), file.getOriginalFilename());

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

}
