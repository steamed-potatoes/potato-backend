package com.potato.api.controller.upload.request;

import com.potato.common.exception.type.ImageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UploadImageRequest {

    @NotNull
    private ImageType type;

}
