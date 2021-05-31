package com.potato.common.utils;

import com.potato.common.exception.model.ValidationException;
import com.potato.common.type.ImageType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileUtilsTest {

    @Test
    void 기존의_확장자는_유지한채_랜덤의_파일_이름이_반환된다() {
        // given
        ImageType type = ImageType.MEMBER_PROFILE;
        String originalFileName = "image.jpeg";

        // when
        String result = FileUtils.createFileUuidNameWithExtension(type, originalFileName);

        // then
        assertThat(result).startsWith(type.getDirectory());
        assertThat(result).endsWith(".jpeg");
    }

    @Test
    void 파일에_확장자가_없으면_VALIDATION_EXCEPTION_이_발생한다() {
        // given
        String originalFileName = "image";

        // when & then
        assertThatThrownBy(() -> FileUtils.createFileUuidNameWithExtension(ImageType.ORGANIZATION_PROFILE, originalFileName)).isInstanceOf(ValidationException.class);
    }

    @Test
    void 허용된_이미지_확장자일경우_정상_통과한다() {
        // given
        String contentType = "image/jpeg";

        // when & then
        FileUtils.validateImageFile(contentType);
    }

    @Test
    void 허용되지_않은_이미지_확장자면_VALIDATION_EXCEPTION_이_발생한다() {
        // given
        String contentType = "image/abc";

        // when & then
        assertThatThrownBy(() -> FileUtils.validateImageFile(contentType)).isInstanceOf(ValidationException.class);
    }

}
