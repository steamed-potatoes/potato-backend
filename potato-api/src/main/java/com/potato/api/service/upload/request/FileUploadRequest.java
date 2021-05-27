package com.potato.api.service.upload.request;

import com.potato.common.type.ImageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUploadRequest {

    @NotNull
    private ImageType type;

    public static FileUploadRequest testInstance(ImageType type) {
        return new FileUploadRequest(type);
    }

}
