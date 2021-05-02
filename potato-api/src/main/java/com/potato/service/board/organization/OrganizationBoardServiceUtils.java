package com.potato.service.board.organization;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardType;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrganizationBoardServiceUtils {

    static OrganizationBoard findOrganizationBoardById(OrganizationBoardRepository repository, Long organizationBoardId) {
        OrganizationBoard organizationBoard = repository.findOrganizationBoardById(organizationBoardId);
        if (organizationBoard == null) {
            throw new NotFoundException(String.format("해당하는 그룹의 게시물 (%s)이 존재하지 않습니다", organizationBoardId), "존재하지 않는 게시물 입니다.");
        }
        return organizationBoard;
    }

    static OrganizationBoard findOrganizationBoardByIdAndSubDomain(OrganizationBoardRepository repository, String subDomain, Long organizationBoardId) {
        OrganizationBoard organizationBoard = repository.findOrganizationBoardByIdAndSubDomain(organizationBoardId, subDomain);
        if (organizationBoard == null) {
            throw new NotFoundException(String.format("해당하는 그룹 (%s)의 게시물 (%s)이 존재하지 않습니다", subDomain, organizationBoardId), "해당 그룹에 존재하지 않는 게시물입니다.");
        }
        return organizationBoard;
    }

    public static List<OrganizationBoard> findOrganizationBoardWithPagination(OrganizationBoardRepository organizationBoardRepository, long lastOrganizationBoardId, int size) {
        if (lastOrganizationBoardId == 0) {
            return organizationBoardRepository.findBoardsOrderByDesc(size);
        }
        return organizationBoardRepository.findBoardsLessThanOrderByIdDescLimit(lastOrganizationBoardId, size);
    }

    public static List<OrganizationBoard> findOrganizationBoardWithPaginationByType(OrganizationBoardRepository organizationBoardRepository, OrganizationBoardType type, long lastOrganizationBoardId, int size) {
        if (lastOrganizationBoardId == 0) {
            return organizationBoardRepository.findBoardsByTypeOrderByDesc(type, size);
        }
        return organizationBoardRepository.findBoardsByTypeLessThanOrderByIdDescLimit(type, lastOrganizationBoardId, size);
    }

    public static void validateExistsBoard(OrganizationBoardRepository organizationBoardRepository, Long organizationBoardId) {
        OrganizationBoard organizationBoard = organizationBoardRepository.findOrganizationBoardById(organizationBoardId);
        if (organizationBoard == null) {
            throw new NotFoundException(String.format("해당하는 그룹의 게시물 (%s)이 존재하지 않습니다", organizationBoardId), "존재하지 않는 게시물 입니다.");
        }
    }

}
