package com.potato.domain.domain.hashtag;

import com.potato.domain.domain.hashtag.repository.BoardHashTagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHashTagRepository extends JpaRepository<BoardHashTag, Long>, BoardHashTagRepositoryCustom {

}
