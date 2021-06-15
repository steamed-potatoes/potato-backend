package com.potato.recruit.controller.board;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BoardControllerValidation {

    public boolean validation (String phoneNumber) {

        String regExp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";

        if (phoneNumber.matches(regExp)) {
            return true;
        } else {
            return false;
        }
    }

}
