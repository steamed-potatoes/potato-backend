package com.potato.domain.organization;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<OrganizationFollower, Long> {
}
