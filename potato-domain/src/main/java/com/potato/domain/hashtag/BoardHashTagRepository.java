package com.potato.domain.hashtag;

import com.potato.domain.hashtag.repository.BoardHashTagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHashTagRepository extends JpaRepository<BoardHashTag, Long>, BoardHashTagRepositoryCustom {

}
