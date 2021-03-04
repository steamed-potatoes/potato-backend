package com.potato.domain.organization;

import com.potato.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
