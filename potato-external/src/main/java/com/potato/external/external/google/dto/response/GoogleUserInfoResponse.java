package com.potato.external.external.google.dto.response;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleUserInfoResponse {

    private String id;

    private String email;

    private String name;

    private String picture;

    private String locale;

    @Builder(builderMethodName = "testBuilder")
    public GoogleUserInfoResponse(String id, String email, String name, String picture, String locale) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.locale = locale;
    }

}
