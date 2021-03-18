package com.potato.service;

import com.potato.domain.adminMember.AdminMember;
import com.potato.domain.adminMember.AdminMemberRepository;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AdminAuthServiceUtils {

    static AdminMember findAdminMemberByEmail(AdminMemberRepository adminMemberRepository, String email) {
        AdminMember findAdminMember = adminMemberRepository.findByEmail(email);
        if (findAdminMember == null) {
            throw new NotFoundException(String.format("존재하지 않는 (%s)의 관리자입니다", email));
        }
        return findAdminMember;
    }

}
