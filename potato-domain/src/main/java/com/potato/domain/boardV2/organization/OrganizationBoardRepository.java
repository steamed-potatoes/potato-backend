package com.potato.domain.boardV2.organization;

import com.potato.domain.boardV2.organization.repository.OrganizationBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationBoardRepository extends JpaRepository<OrganizationBoard, Long>, OrganizationBoardRepositoryCustom {

}
