package com.potato.domain.repository;

import com.potato.domain.AdminMember;

public interface AdminMemberRepositoryCustom {

    AdminMember findByEmail(String email);

}
