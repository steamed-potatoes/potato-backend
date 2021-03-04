package com.potato.domain.comment;

import com.potato.domain.comment.repository.BoardCommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>, BoardCommentRepositoryCustom {

}
