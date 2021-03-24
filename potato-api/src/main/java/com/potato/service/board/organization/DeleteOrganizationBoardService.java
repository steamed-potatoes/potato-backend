package com.potato.service.board.organization;

import com.potato.domain.board.organization.DeleteOrganizationBoard;
import com.potato.domain.board.organization.DeleteOrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteOrganizationBoardService {

    private final DeleteOrganizationBoardRepository deleteOrganizationBoardRepository;

    @Transactional
    public void backUpOrganizationBoard(OrganizationBoard organizationBoard, Long memberId) {
        deleteOrganizationBoardRepository.save(DeleteOrganizationBoard.newBackUpInstance(organizationBoard, memberId));
    }

}