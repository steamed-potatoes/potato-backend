package com.potato.domain.domain.organization;

import com.potato.common.exception.model.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

import static com.potato.common.exception.ErrorCode.VALIDATION_SUBDOMAIN_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class SubDomainValidator {

    private static final Pattern SUB_DOMAIN_PATTERN = Pattern.compile("^[a-zA-Z0-9]*$", Pattern.CASE_INSENSITIVE);

    static void validateSubDomain(String subDomain) {
        if (!SUB_DOMAIN_PATTERN.matcher(subDomain).matches()) {
            throw new ValidationException(String.format("허용되지 않은 SubDomain (%s) 입니다", subDomain), VALIDATION_SUBDOMAIN_EXCEPTION);
        }
    }

}
