package com.potato.domain.member;

import com.potato.exception.model.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ClassNumberTest {

    @Test
    void 정상적인_아홉자리와_양수인_학번() {
        //given
        Integer classNumber = 201610323;

        //when
        ClassNumber classNumber1 = ClassNumber.of(classNumber);

        //then
        assertThat(classNumber1.getClassNumber()).isEqualTo(classNumber);
    }

    @Test
    void 학번이_양수가_아닐경우() {
        // given
        Integer classNumber = -201610323;

        // when & then
        assertThatThrownBy(
            () -> ClassNumber.of(classNumber)
        ).isInstanceOf(ValidationException.class);
    }

    @Test
    void 학번이_아홉자리가_아닐경우() {
        // given
        Integer classNumber = 1234;

        // when & then
        assertThatThrownBy(
            () -> ClassNumber.of(classNumber)
        ).isInstanceOf(ValidationException.class);
    }

    @Test
    void 유효하지_않은_학번일_경우_너무_옛날() {
        // given
        Integer classNumber = 195299999;

        // when & then
        assertThatThrownBy(
            () -> ClassNumber.of(classNumber)
        ).isInstanceOf(ValidationException.class);
    }

    @Test
    void 미래의_학번() {
        // given
        Integer classNumber = 2030000000;

        // when & then
        assertThatThrownBy(
            () -> ClassNumber.of(classNumber)
        ).isInstanceOf(ValidationException.class);
    }

    @Test
    void 유효한_학번일_경우() {
        //given
        Integer classNumber = 202110302;

        // when
        ClassNumber classNumber1 = ClassNumber.of(classNumber);

        // then
        assertThat(classNumber1.getClassNumber()).isEqualTo(classNumber);
    }

}
