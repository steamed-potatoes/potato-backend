package com.potato.domain.member;

import com.potato.exception.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

import static com.potato.exception.ErrorCode.VALIDATION_CLASS_NUMBER_EXCEPTION;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ClassNumber {

    private final static int DIGIT = 100_000;
    private final static int MIN_YEAR = 1953;

    @Column(length = 9)
    private Integer classNumber;

    private ClassNumber(Integer classNumber) {
        validateClassNumberFormat(classNumber);
        this.classNumber = classNumber;
    }

    private void validateClassNumberFormat(Integer classNumber) {
        validateIsInRange(classNumber);
        validateLength(classNumber);
    }

    private void validateIsInRange(Integer classNumber) {
        if (classNumber < getMinClassNumber() || classNumber >= getMaxClassNumber()) {
            throw new ValidationException(String.format("잘못된 학번 (%s) 입니다. (학번의 범위를 벗어났습니다)", classNumber), VALIDATION_CLASS_NUMBER_EXCEPTION);
        }
    }

    private int getMinClassNumber() {
        return MIN_YEAR * DIGIT;
    }

    private int getMaxClassNumber() {
        final int currentYear = LocalDateTime.now().getYear() + 1;
        return currentYear * DIGIT;
    }

    private void validateLength(Integer classNumber) {
        if (String.valueOf(classNumber).length() != 9) {
            throw new ValidationException(String.format("잘못된 학번 (%s)입니다. (학번이 9자리가 아닙니다)", classNumber), VALIDATION_CLASS_NUMBER_EXCEPTION);
        }
    }

    public static ClassNumber of(Integer classNumber) {
        return new ClassNumber(classNumber);
    }

}
