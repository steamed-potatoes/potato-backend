package com.potato.api;

import com.potato.domain.PotatoDomainRoot;
import com.potato.external.PotatoExternalRoot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {PotatoApiApplication.class, PotatoExternalRoot.class, PotatoDomainRoot.class})
public class PotatoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PotatoApiApplication.class, args);
    }

}
