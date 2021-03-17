package com.potato.domain.adminMember;

import com.potato.domain.adminMember.repository.AdminMemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMemberRepository extends JpaRepository<AdminMember, Long>, AdminMemberRepositoryCustom {
}
