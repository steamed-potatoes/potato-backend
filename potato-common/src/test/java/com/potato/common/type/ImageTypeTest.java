package com.potato.common.type;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageTypeTest {

    @Test
    void 파일_이름을_입력하면_지정된_디렉터리와_함께_파일_이름이_반환된다() {
        // given
        String fileName = "fileName.jpeg";
        ImageType type = ImageType.MEMBER_PROFILE;

        // when
        String result = type.getFileNameWithDirectory(fileName);

        // then
        assertThat(result).isEqualTo("member_profile/fileName.jpeg");
    }

}
