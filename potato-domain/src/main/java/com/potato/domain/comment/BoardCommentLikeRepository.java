package com.potato.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentLikeRepository extends JpaRepository<BoardCommentLike, Long> {
}
