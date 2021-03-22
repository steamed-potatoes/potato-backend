package com.potato.domain.member;

import com.potato.exception.ValidationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ClassNumberTest {

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
        //given
        Integer classNumber = -201610323;

        // when & then
        assertThatThrownBy(
            () -> ClassNumber.of(classNumber)
        ).isInstanceOf(ValidationException.class).hasMessage(String.format("(%d) 학번은 양수가 아닙니다.", classNumber));
    }

    @Test
    void 학번이_아홉자리가_아닐경우() {
        //given
        Integer classNumber = 1234;

        // when & then
        assertThatThrownBy(
            () -> ClassNumber.of(classNumber)
        ).isInstanceOf(ValidationException.class);
    }

}
