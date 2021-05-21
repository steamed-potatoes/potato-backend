package com.potato.admin;

import com.potato.domain.PotatoDomainRoot;
import com.potato.external.PotatoExternalRoot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {PotatoAdminApplication.class, PotatoExternalRoot.class, PotatoDomainRoot.class})
public class PotatoAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(PotatoAdminApplication.class, args);
    }

}
