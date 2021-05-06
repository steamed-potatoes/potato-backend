package com.potato.domain.board.admin.repository;

import com.potato.domain.board.admin.AdminBoard;

import java.time.LocalDate;
import java.util.List;

public interface AdminBoardRepositoryCustom {

    List<AdminBoard> findAllBetweenDate(LocalDate startDate, LocalDate endDate);

    AdminBoard findAdminBoardById(Long adminBoardId);

}
