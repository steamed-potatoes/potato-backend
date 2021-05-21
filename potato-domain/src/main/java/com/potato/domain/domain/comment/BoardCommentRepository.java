package com.potato.domain.domain.comment;

import com.potato.domain.domain.comment.repository.BoardCommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>, BoardCommentRepositoryCustom {

}
