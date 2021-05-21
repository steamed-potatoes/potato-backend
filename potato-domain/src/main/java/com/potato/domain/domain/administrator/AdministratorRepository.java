package com.potato.domain.domain.administrator;

import com.potato.domain.domain.administrator.repository.AdministratorRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Long>, AdministratorRepositoryCustom {
}
