package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardRepository;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.board.dto.request.CreateBoardRequest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import com.potato.service.organization.OrganizationServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardAdminService {

    private final BoardRepository boardRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public BoardInfoResponse createBoard(String subDomain, CreateBoardRequest request, Long memberId) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        Board board = boardRepository.save(request.toEntity(organization.getId(), memberId));
        return BoardInfoResponse.of(board);
    }

}
