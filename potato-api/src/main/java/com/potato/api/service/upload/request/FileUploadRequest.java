package com.potato.api.service.upload.request;

import com.potato.common.type.ImageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUploadRequest {

    @NotNull
    private ImageType type;

}
