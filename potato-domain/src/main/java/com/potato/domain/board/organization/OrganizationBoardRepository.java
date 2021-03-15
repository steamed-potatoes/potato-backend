package com.potato.domain.board.organization;

import com.potato.domain.board.organization.repository.OrganizationBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationBoardRepository extends JpaRepository<OrganizationBoard, Long>, OrganizationBoardRepositoryCustom {

}
