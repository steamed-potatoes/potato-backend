package com.potato.api.controller.upload;

import com.potato.api.controller.ApiResponse;
import com.potato.api.service.upload.FileUploadService;
import com.potato.api.service.upload.request.FileUploadRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "이미지 파일을 업로드하는 API")
    @PostMapping("/api/v1/upload")
    public ApiResponse<String> uploadFile(@Valid FileUploadRequest request, @RequestPart MultipartFile file) {
        return ApiResponse.success(fileUploadService.uploadImage(request, file));
    }

    @Operation(summary = "여러 이미지 파일을 업로드하는 API")
    @PostMapping("/api/v1/upload/list")
    public ApiResponse<List<String>> uploadFiles(@Valid FileUploadRequest request, @RequestPart List<MultipartFile> files) {
        return ApiResponse.success(fileUploadService.uploadImages(request, files));
    }

}
