package com.potato.domain.board.admin;

import com.potato.domain.board.admin.repository.AdminBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBoardRepository extends JpaRepository<AdminBoard, Long>, AdminBoardRepositoryCustom {

}
