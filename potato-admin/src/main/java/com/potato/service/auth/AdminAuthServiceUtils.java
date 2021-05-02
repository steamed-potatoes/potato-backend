package com.potato.service.auth;

import com.potato.domain.administrator.Administrator;
import com.potato.domain.administrator.AdministratorRepository;
import com.potato.exception.model.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminAuthServiceUtils {

    static Administrator findAdminMemberByEmail(AdministratorRepository administratorRepository, String email) {
        Administrator findAdministrator = administratorRepository.findAdminByEmail(email);
        if (findAdministrator == null) {
            throw new NotFoundException(String.format("존재하지 않는 관리자 (%s) 입니다", email));
        }
        return findAdministrator;
    }

    public static void validateExistAdminMember(AdministratorRepository administratorRepository, Long adminMemberId) {
        Administrator findAdministrator = administratorRepository.findAdminById(adminMemberId);
        if (findAdministrator == null) {
            throw new NotFoundException(String.format("존재하지 않는 관리자 (%s) 입니다", adminMemberId));
        }
    }

}
