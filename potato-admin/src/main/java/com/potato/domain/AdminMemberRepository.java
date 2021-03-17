package com.potato.domain;

import com.potato.domain.repository.AdminMemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMemberRepository extends JpaRepository<AdminMember, Long>, AdminMemberRepositoryCustom {

}
