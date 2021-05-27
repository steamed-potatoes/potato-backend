package com.potato.common.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ImageType {

    MEMBER_PROFILE("member_profile"),
    ORGANIZATION_PROFILE("organization_profile"),
    BOARD_IMAGE("board_image");

    private final String directory;

    public String getFileNameWithDirectory(String fileName) {
        return this.directory.concat("/").concat(fileName);
    }

}
