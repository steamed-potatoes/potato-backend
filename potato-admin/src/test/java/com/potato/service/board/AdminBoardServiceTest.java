package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardRepository;
import com.potato.domain.board.DeleteBoard;
import com.potato.domain.board.DeleteBoardRepository;
import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.domain.board.admin.DeleteAdminBoard;
import com.potato.domain.board.admin.DeleteAdminBoardRepository;
import com.potato.domain.board.organization.*;
import com.potato.exception.model.NotFoundException;
import com.potato.service.AdminSetupTest;
import com.potato.service.board.dto.request.CreateAdminBoardRequest;
import com.potato.service.board.dto.request.UpdateAdminBoardRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class AdminBoardServiceTest extends AdminSetupTest {

    @Autowired
    private AdminBoardRepository adminBoardRepository;

    @Autowired
    private AdminBoardService adminBoardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private DeleteAdminBoardRepository deleteAdminBoardRepository;

    @Autowired
    private DeleteBoardRepository deleteBoardRepository;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @Autowired
    private DeleteOrganizationBoardRepository deleteOrganizationBoardRepository;

    @AfterEach
    void cleanup() {
        super.cleanUp();
        adminBoardRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        organizationBoardRepository.deleteAllInBatch();
        deleteAdminBoardRepository.deleteAllInBatch();
        deleteOrganizationBoardRepository.deleteAllInBatch();
        deleteBoardRepository.deleteAllInBatch();
    }

    @Test
    void 관리자_게시글에_글을_쓴다() {
        // given
        String content = "content";
        String title = "title";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 9, 3, 12, 12);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 9, 5, 12, 12);
        CreateAdminBoardRequest request = CreateAdminBoardRequest.testBuilder()
            .content(content)
            .title(title)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();

        // when
        adminBoardService.createAdminBoard(request, adminMemberId);

        // then
        List<AdminBoard> adminBoardList = adminBoardRepository.findAll();
        assertThat(adminBoardList).hasSize(1);
        assertThat(adminBoardList.get(0).getContent()).isEqualTo(content);
        assertThat(adminBoardList.get(0).getAdministratorId()).isEqualTo(adminMemberId);

        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getTitle()).isEqualTo(title);
        assertThat(boardList.get(0).getStartDateTime()).isEqualTo(startDateTime);
        assertThat(boardList.get(0).getEndDateTime()).isEqualTo(endDateTime);
    }

    @Test
    void 관리자가_게시글을_수정한다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 3, 0, 0);
        AdminBoard adminBoard = AdminBoard.builder()
            .administratorId(adminMemberId)
            .title("학사")
            .content("학사행정입니다.")
            .startDateTime(LocalDateTime.of(2020,3,30,0, 0))
            .endDateTime(LocalDateTime.of(2020,4,30,0, 0))
            .build();
        adminBoardRepository.save(adminBoard);

        String title = "학사행정";
        String content = "내용";
        UpdateAdminBoardRequest request = UpdateAdminBoardRequest.testBuilder()
            .adminBoardId(adminBoard.getId())
            .title(title)
            .content(content)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();

        // when
        adminBoardService.updateAdminBoard(request);

        // then
        List<AdminBoard> adminBoardList = adminBoardRepository.findAll();
        assertThat(adminBoardList).hasSize(1);
        assertThat(adminBoardList.get(0).getContent()).isEqualTo(content);
        assertThat(adminBoardList.get(0).getAdministratorId()).isEqualTo(adminMemberId);

        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getTitle()).isEqualTo(title);
        assertThat(boardList.get(0).getStartDateTime()).isEqualTo(startDateTime);
        assertThat(boardList.get(0).getEndDateTime()).isEqualTo(endDateTime);
    }

    @Test
    void 관리자_게시물을_수정할때_해당하는_게시물이_없는경우_에러가_발생한다() {
        // given
        UpdateAdminBoardRequest request = UpdateAdminBoardRequest.testBuilder()
            .adminBoardId(999L)
            .title("제목")
            .build();

        // when & then
        assertThatThrownBy(() -> adminBoardService.updateAdminBoard(request)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 관리자가_게시글을_삭제하면_백업되고_삭제한다() {
        // given
        String title = "학사";
        String content = "학사행정입니다.";
        AdminBoard adminBoard = AdminBoard.builder()
            .administratorId(adminMemberId)
            .title(title)
            .content(content)
            .startDateTime(LocalDateTime.of(2021, 4, 1, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 4, 3, 0, 0))
            .build();
        adminBoardRepository.save(adminBoard);

        // when
        adminBoardService.deleteAdminBoard(adminBoard.getId(), adminMemberId);

        // then
        List<DeleteAdminBoard> deleteAdminBoardList = deleteAdminBoardRepository.findAll();
        assertThat(deleteAdminBoardList).hasSize(1);
        assertDeleteAdminBoard(deleteAdminBoardList.get(0), content, adminMemberId, adminBoard.getId());

        List<AdminBoard> adminBoardList = adminBoardRepository.findAll();
        assertThat(adminBoardList).isEmpty();

        List<DeleteBoard> deleteBoardList = deleteBoardRepository.findAll();
        assertThat(deleteBoardList).hasSize(1);
        assertDeleteBoard(deleteBoardList.get(0), title, adminMemberId);
    }

    @Test
    void 관리자_게시물을_삭제할때_해당하는_게시물이_없는경우_에러가_발생한다() {
        // when & then
        assertThatThrownBy(() -> adminBoardService.deleteAdminBoard(999L, adminMemberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void 관리자가_일반유저들의_게시글을_삭제한다() {
        //given
        String subDomain = "subDomain";
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 1L, "title", OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        //when
        adminBoardService.deleteOrganizationBoard(subDomain, 2L, organizationBoard.getId());

        //then
        List<DeleteOrganizationBoard> deleteOrganizationBoardList = deleteOrganizationBoardRepository.findAll();
        List<DeleteBoard> deleteBoardList = deleteBoardRepository.findAll();
        assertThat(deleteBoardList).hasSize(1);
        assertThat(deleteOrganizationBoardList).hasSize(1);
        assertThat(deleteOrganizationBoardList.get(0).getSubDomain()).isEqualTo(subDomain);
    }

    @Test
    public void 그룹게시물이_없을_경우에_관리자가_삭제하려고_하면_애러가_발생한다() {
        // when & then
        assertThatThrownBy(
            () -> adminBoardService.deleteOrganizationBoard("subDomain", 1L, 1L)
        ).isInstanceOf(NotFoundException.class);
    }

    private void assertDeleteAdminBoard(DeleteAdminBoard deleteAdminBoard, String content, Long adminMemberId, Long adminBoardId) {
        assertThat(deleteAdminBoard.getContent()).isEqualTo(content);
        assertThat(deleteAdminBoard.getBackUpId()).isEqualTo(adminBoardId);
        assertThat(deleteAdminBoard.getDeleteAdministratorId()).isEqualTo(adminMemberId);
    }

    private void assertDeleteBoard(DeleteBoard deleteBoard, String title, Long adminMemberId) {
        assertThat(deleteBoard.getTitle()).isEqualTo(title);
        assertThat(deleteBoard.getMemberId()).isEqualTo(adminMemberId);
    }

}
