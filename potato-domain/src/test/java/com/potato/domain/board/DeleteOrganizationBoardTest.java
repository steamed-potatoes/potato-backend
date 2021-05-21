package com.potato.domain.board;

import com.potato.domain.domain.board.organization.DeleteOrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.domain.board.organization.OrganizationBoardCreator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteOrganizationBoardTest {

    @Test
    void 그룹장이_삭제한_OrganizationBoard의_정보들로_DeleteOrganizationBoard가_생성된다() {
        // given
        String subDomain = "subDomain";
        Long memberId = 100L;
        String title = "그룹 게시글";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 5, 21, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 5, 22, 0, 0, 0);
        OrganizationBoardCategory category = OrganizationBoardCategory.RECRUIT;
        Long deletedMemberId = 90L;

        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, title, startDateTime, endDateTime, category);

        // when
        DeleteOrganizationBoard deleteOrganizationBoard = DeleteOrganizationBoard.newBackUpInstance(organizationBoard, deletedMemberId);

        // then
        assertDeleteOrganizationBoard(deleteOrganizationBoard, subDomain, memberId, title, startDateTime, endDateTime, category, deletedMemberId, null);
    }

    @Test
    void 관리자가_삭제한_OrganizationBoard의_정보들로_DeleteOrganizationBoard가_생성된다() {
        // given
        String subDomain = "subDomain";
        Long memberId = 100L;
        String title = "그룹 게시글";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 5, 21, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 5, 22, 0, 0, 0);
        OrganizationBoardCategory category = OrganizationBoardCategory.RECRUIT;
        Long deletedMemberId = 90L;

        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, title, startDateTime, endDateTime, category);

        // when
        DeleteOrganizationBoard deleteOrganizationBoard = DeleteOrganizationBoard.newBackUpInstanceByAdmin(organizationBoard, deletedMemberId);

        // then
        assertDeleteOrganizationBoard(deleteOrganizationBoard, subDomain, memberId, title, startDateTime, endDateTime, category, null, deletedMemberId);
    }

    private void assertDeleteOrganizationBoard(DeleteOrganizationBoard deleteOrganizationBoard, String subDomain, Long memberId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, OrganizationBoardCategory category, Long deletedMemberId, Long deleteAdminMemberId) {
        assertThat(deleteOrganizationBoard.getSubDomain()).isEqualTo(subDomain);
        assertThat(deleteOrganizationBoard.getMemberId()).isEqualTo(memberId);
        assertThat(deleteOrganizationBoard.getTitle()).isEqualTo(title);
        assertThat(deleteOrganizationBoard.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(deleteOrganizationBoard.getEndDateTime()).isEqualTo(endDateTime);
        assertThat(deleteOrganizationBoard.getCategory()).isEqualTo(category);
        assertThat(deleteOrganizationBoard.getDeletedMemberId()).isEqualTo(deletedMemberId);
        assertThat(deleteOrganizationBoard.getDeletedAdminMemberId()).isEqualTo(deleteAdminMemberId);
    }

}
