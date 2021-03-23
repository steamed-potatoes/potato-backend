package com.potato.domain.administrator;

import com.potato.domain.administrator.repository.AdministratorRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Long>, AdministratorRepositoryCustom {
}
