package com.potato.domain.domain.member;

import com.potato.common.exception.ErrorCode;
import com.potato.common.exception.model.ValidationException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Email {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Column(nullable = false, length = 50)
    private String email;

    private Email(String email) {
        verifyEmailFormat(email);
        this.email = email;
    }

    private void verifyEmailFormat(String email) {
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new ValidationException(String.format("(%s)은 이메일 형식이 아닙니다", email), ErrorCode.VALIDATION_EMAIL_FORMAT_EXCEPTION);
        }
    }

    public static Email of(String email) {
        return new Email(email);
    }

}
