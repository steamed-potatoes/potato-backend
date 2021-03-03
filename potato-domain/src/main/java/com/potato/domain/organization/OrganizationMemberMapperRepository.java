package com.potato.domain.organization;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 이 레포지토리는 테스트에서만 사용되어야 함.
 */
public interface OrganizationMemberMapperRepository extends JpaRepository<OrganizationMemberMapper, Long> {

}
