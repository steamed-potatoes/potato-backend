package com.potato.domain.organization;

import com.potato.domain.member.Member;
import com.potato.domain.organization.repository.OrganizationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Member>, OrganizationRepositoryCustom {

}
