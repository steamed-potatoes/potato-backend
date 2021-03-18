package com.potato.domain.adminMember.repository;

import com.potato.domain.adminMember.AdminMember;

public interface AdminMemberRepositoryCustom {

    AdminMember findAdminByEmail(String email);

	AdminMember findAdminById(Long adminMemberId);

}
