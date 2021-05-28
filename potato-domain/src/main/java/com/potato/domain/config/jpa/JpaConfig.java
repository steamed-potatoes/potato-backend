package com.potato.domain.config.jpa;

import com.potato.domain.PotatoDomainRoot;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackageClasses = {PotatoDomainRoot.class})
@EnableJpaRepositories(basePackageClasses = {PotatoDomainRoot.class})
@EnableJpaAuditing
@Configuration
public class JpaConfig {

}
