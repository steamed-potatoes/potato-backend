package com.potato.domain.domain.organization;

import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.organization.repository.OrganizationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Member>, OrganizationRepositoryCustom {

}
