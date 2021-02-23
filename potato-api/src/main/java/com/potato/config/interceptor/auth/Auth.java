package com.potato.config.interceptor.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auth {

    Role role() default Role.USER;

    enum Role {
        USER,
        ORGANIZATION_ADMIN
    }

}
