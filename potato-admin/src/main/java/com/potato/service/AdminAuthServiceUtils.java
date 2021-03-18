package com.potato.service;

import com.potato.domain.adminMember.AdminMember;
import com.potato.domain.adminMember.AdminMemberRepository;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminAuthServiceUtils {

    static AdminMember findAdminMemberByEmail(AdminMemberRepository adminMemberRepository, String email) {
        AdminMember findAdminMember = adminMemberRepository.findAdminByEmail(email);
        if (findAdminMember == null) {
            throw new NotFoundException(String.format("존재하지 않는 관리자 (%s) 입니다", email));
        }
        return findAdminMember;
    }

    public static void validateExistAdminMember(AdminMemberRepository adminMemberRepository, Long adminMemberId) {
        AdminMember findAdminMember = adminMemberRepository.findAdminById(adminMemberId);
        if (findAdminMember == null) {
            throw new NotFoundException(String.format("존재하지 않는 관리자 (%s) 입니다", adminMemberId));
        }
    }

}
