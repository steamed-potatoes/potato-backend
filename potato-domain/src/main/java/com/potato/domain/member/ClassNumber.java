package com.potato.domain.member;

import com.potato.exception.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ClassNumber {

    @Column(nullable = false, length = 9)
    private Integer classNumber;

    private ClassNumber(Integer classNumber) {
        validateIsPositive(classNumber);
        validateIsNineLength(classNumber);
        this.classNumber = classNumber;
    }

    private void validateIsPositive(Integer classNumber) {
        if (classNumber < 0) {
            throw new ValidationException(String.format("(%d) 학번은 양수가 아닙니다.", classNumber), "학번이 양수가 아닙니다.");
        }
    }

    private void validateIsNineLength(Integer classNumber) {
        int length = (int) (Math.log10(classNumber) + 1);
        if (length != 9) {
            throw new ValidationException(String.format("(%d) 학번은 9자리가 아닙니다", classNumber), "학번이 9자리수가 아닙니다.");
        }
    }

    public static ClassNumber of(Integer classNumber) {
        return new ClassNumber(classNumber);
    }

}
