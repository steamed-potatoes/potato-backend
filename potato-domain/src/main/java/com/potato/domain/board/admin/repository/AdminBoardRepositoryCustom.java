package com.potato.domain.board.admin.repository;

import com.potato.domain.board.admin.AdminBoard;

import java.time.LocalDate;
import java.util.List;

public interface AdminBoardRepositoryCustom {

    List<AdminBoard> findBetweenDate(LocalDate startDate, LocalDate endDate);

}
