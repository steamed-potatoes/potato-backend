package com.potato.domain.domain.administrator.repository;

import com.potato.domain.domain.administrator.Administrator;

public interface AdministratorRepositoryCustom {

    Administrator findAdminByEmail(String email);

	Administrator findAdminById(Long adminMemberId);

}
