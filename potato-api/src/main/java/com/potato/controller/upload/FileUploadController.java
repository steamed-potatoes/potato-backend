package com.potato.controller.upload;

import com.potato.service.upload.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "이미지 파일을 업로드하는 API")
    @PostMapping("/api/v1/upload")
    public String uploadFile(@RequestPart MultipartFile file) {
        return fileUploadService.uploadImageToStorage(file);
    }

}
