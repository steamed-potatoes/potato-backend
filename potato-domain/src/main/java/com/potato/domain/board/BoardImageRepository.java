package com.potato.domain.board;

import com.potato.domain.board.repository.BoardImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long>, BoardImageRepositoryCustom {
}
