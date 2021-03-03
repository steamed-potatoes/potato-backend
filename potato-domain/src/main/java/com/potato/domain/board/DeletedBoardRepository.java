package com.potato.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedBoardRepository extends JpaRepository<DeletedBoard, Long> {

}
