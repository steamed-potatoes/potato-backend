package com.potato.domain.adminMember.repository;

import com.potato.domain.adminMember.AdminMember;

public interface AdminMemberRepositoryCustom {

    AdminMember findByEmail(String email);

}
