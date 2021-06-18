package com.potato.recruit.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@Getter
@Embeddable
public class PhoneNumber {

    private static final Pattern tellPattern = Pattern.compile("^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})[.-]?(\\d{3,4})[.-]?(\\d{4})");

    private String mobileCarrier;

    private String middleNumber;

    private String lastNumber;

    public PhoneNumber(String phoneNumber) {
        String[] phoneArray = phoneNumberSplit(phoneNumber);
        this.mobileCarrier = phoneArray[0];
        this.middleNumber = phoneArray[1];
        this.lastNumber = phoneArray[2];
    }

    public static String[] phoneNumberSplit(String phoneNumber) {
        Matcher matcher = tellPattern.matcher(phoneNumber);
        if (matcher.matches()) {
            return new String[]{matcher.group(1), matcher.group(2), matcher.group(3)};
        }
        throw new IllegalArgumentException(String.format("잘못된 전화번호 (%s) 입니다", phoneNumber));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(mobileCarrier, that.mobileCarrier) && Objects.equals(middleNumber, that.middleNumber) && Objects.equals(lastNumber, that.lastNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mobileCarrier, middleNumber, lastNumber);
    }

}
