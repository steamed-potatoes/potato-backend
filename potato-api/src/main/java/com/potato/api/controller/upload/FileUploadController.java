package com.potato.api.controller.upload;

import com.potato.api.controller.ApiResponse;
import com.potato.api.service.upload.FileUploadService;
import com.potato.api.controller.upload.request.UploadImageRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "이미지 파일을 업로드하는 API")
    @PostMapping("/api/v1/upload")
    public ApiResponse<String> uploadFile(@Valid UploadImageRequest request, @RequestPart MultipartFile file) {
        return ApiResponse.success(fileUploadService.uploadImageToStorage(request.getType(), file));
    }

}
