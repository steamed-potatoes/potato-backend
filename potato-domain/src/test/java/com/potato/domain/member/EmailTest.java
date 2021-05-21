package com.potato.domain.member;

import com.potato.domain.domain.member.Email;
import com.potato.common.exception.model.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void 이메일_정규식_테스트_정상_이메일_포맷() {
        // given
        String email = "will.seungho@gmail.com";

        // when
        Email result = Email.of(email);

        // then
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    void 이메일_정규식_테스트_도메인_없는_주소() {
        // given
        String email = "will.seungho";

        // when & then
        assertThatThrownBy(() -> Email.of(email)).isInstanceOf(ValidationException.class);
    }

    @Test
    void 이메일_정규식_테스트_최상위_도메인_없는_주소() {
        // given
        String email = "will.seungho@gmail";

        // when & then
        assertThatThrownBy(() -> Email.of(email)).isInstanceOf(ValidationException.class);
    }

    @Test
    void 이메일_정규식_테스트_로컬영역_없는_주소() {
        // given
        String email = "@gmail.com";

        // when & then
        assertThatThrownBy(() -> Email.of(email)).isInstanceOf(ValidationException.class);
    }

    @Test
    void 이메일_동등성비교_이메일이_같은경우_true() {
        // given
        String value = "will.seungho@gmail.com";
        Email email = Email.of(value);

        // when
        boolean result = Email.of(value).equals(email);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 이메일_동등성비교_이메일이_다른경우_false() {
        // given
        Email email = Email.of("will.seungho@gmail.com");

        // when
        boolean result = Email.of("ksh980212@gmail.com").equals(email);

        // then
        assertThat(result).isFalse();
    }

}
