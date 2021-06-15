package com.potato.recruit.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@Getter
@Embeddable
public class PhoneNumber {

    private String mobileCarrier;

    private String middleNumber;

    private String lastNumber;

    private String phoneNumber;

    public PhoneNumber (String phoneNumber) {

        String[] phoneArray = phoneNumberSplit(phoneNumber);

        this.phoneNumber = phoneNumber;
        this.mobileCarrier = phoneArray[0];
        this.middleNumber = phoneArray[1];
        this.lastNumber = phoneArray[2];

    }

    public static String[] phoneNumberSplit(String phoneNumber) {

        Pattern tellPattern = Pattern.compile("^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})[.-]?(\\d{3,4})[.-]?(\\d{4})");

        Matcher matcher = tellPattern.matcher(phoneNumber);

        if (matcher.matches()) {

            return new String[]{matcher.group(1), matcher.group(2), matcher.group(3)};

        } else {

            String str1 = phoneNumber.substring(0, 3);
            String str2 = phoneNumber.substring(3, 7);
            String str3 = phoneNumber.substring(7, 11);

            return new String[]{str1, str2, str3};
        }
    }
}
