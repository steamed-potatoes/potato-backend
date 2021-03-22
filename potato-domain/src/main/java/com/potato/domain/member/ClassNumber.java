package com.potato.domain.member;

import com.potato.exception.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ClassNumber {

    private final static int DIGIT = 100_000;
    private final static int MIN_YEAR = 1953;

    @Column(nullable = false, length = 9)
    private Integer classNumber;

    private ClassNumber(Integer classNumber) {
        validateClassNumberFormat(classNumber);
        this.classNumber = classNumber;
    }

    private void validateClassNumberFormat(Integer classNumber) {
        validateClassNumberRange(classNumber);
        validateClassNumberLength(classNumber);
    }

    private void validateClassNumberRange(Integer classNumber) {
        if (classNumber < getMinClassNumber() || classNumber >= getMaxClassNumber()) {
            throw new ValidationException(String.format("학번 (%d)은 잘못되었습니다.", classNumber), "잘못된 학번입니다.");
        }
    }

    private int getMinClassNumber() {
        return MIN_YEAR * DIGIT;
    }

    private int getMaxClassNumber() {
        final int currentYear = LocalDateTime.now().plusYears(1).getYear();
        return currentYear * DIGIT;
    }

    private void validateClassNumberLength(Integer classNumber) {
        if (String.valueOf(classNumber).length() != 9) {
            throw new ValidationException(String.format("(%d) 학번은 9자리가 아닙니다", classNumber), "잘못된 학번입니다.");
        }
    }

    public static ClassNumber of(Integer classNumber) {
        return new ClassNumber(classNumber);
    }

}
