package com.potato.domain.administrator.repository;

import com.potato.domain.administrator.Administrator;

public interface AdministratorRepositoryCustom {

    Administrator findAdminByEmail(String email);

	Administrator findAdminById(Long adminMemberId);

}
