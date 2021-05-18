package com.potato.service.board;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
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
    private OrganizationBoardRepository organizationBoardRepository;

    @Autowired
    private DeleteOrganizationBoardRepository deleteOrganizationBoardRepository;

    @AfterEach
    void cleanup() {
        super.cleanUp();
        adminBoardRepository.deleteAllInBatch();
        organizationBoardRepository.deleteAllInBatch();
        deleteOrganizationBoardRepository.deleteAllInBatch();
    }

    @Test
    void 관리자_게시글에_글을_쓴다() {
        // given
        String content = "content";
        String title = "title";
        String imageUrl = "http://image.com";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 9, 3, 12, 12);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 9, 5, 12, 12);
        CreateAdminBoardRequest request = CreateAdminBoardRequest.testBuilder()
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();

        // when
        adminBoardService.createAdminBoard(request, adminMemberId);

        // then
        List<AdminBoard> adminBoardList = adminBoardRepository.findAll();
        assertThat(adminBoardList).hasSize(1);
        assertAdminBoard(adminBoardList.get(0), title, content, imageUrl, startDateTime, endDateTime, adminMemberId);
    }

    @Test
    void 관리자가_게시글을_수정한다() {
        // given
        AdminBoard adminBoard = AdminBoard.builder()
            .administratorId(adminMemberId)
            .title("학사")
            .content("학사행정입니다.")
            .startDateTime(LocalDateTime.of(2020, 3, 30, 0, 0))
            .endDateTime(LocalDateTime.of(2020, 4, 30, 0, 0))
            .build();
        adminBoardRepository.save(adminBoard);

        String title = "학사행정";
        String content = "내용";
        String imageUrl = "imageUrl";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 3, 0, 0);

        UpdateAdminBoardRequest request = UpdateAdminBoardRequest.testBuilder()
            .adminBoardId(adminBoard.getId())
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();

        // when
        adminBoardService.updateAdminBoard(request);

        // then
        List<AdminBoard> adminBoardList = adminBoardRepository.findAll();
        assertThat(adminBoardList).hasSize(1);
        assertAdminBoard(adminBoardList.get(0), title, content, imageUrl, startDateTime, endDateTime, adminMemberId);
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
    public void 관리자가_일반유저들의_게시글을_삭제한다() {
        //given
        String subDomain = "subDomain";
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 1L, "title", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        //when
        adminBoardService.deleteOrganizationBoard(subDomain, organizationBoard.getId(), adminMemberId);

        //then
        List<DeleteOrganizationBoard> deleteOrganizationBoardList = deleteOrganizationBoardRepository.findAll();
        assertThat(deleteOrganizationBoardList).hasSize(1);
        assertDeleteOrganizationBoard(deleteOrganizationBoardList.get(0), organizationBoard, adminMemberId);
        assertThat(deleteOrganizationBoardList.get(0).getSubDomain()).isEqualTo(subDomain);
        assertDeleteInfo(deleteOrganizationBoardList.get(0), adminMemberId);
    }

    @Test
    public void 그룹게시물이_없을_경우에_관리자가_삭제하려고_하면_애러가_발생한다() {
        // when & then
        assertThatThrownBy(
            () -> adminBoardService.deleteOrganizationBoard("subDomain", 1L, adminMemberId)
        ).isInstanceOf(NotFoundException.class);
    }

    private void assertAdminBoard(AdminBoard adminBoard, String title, String content, String imageUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, Long adminMemberId) {
        assertThat(adminBoard.getAdministratorId()).isEqualTo(adminMemberId);
        assertThat(adminBoard.getTitle()).isEqualTo(title);
        assertThat(adminBoard.getContent()).isEqualTo(content);
        assertThat(adminBoard.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(adminBoard.getEndDateTime()).isEqualTo(endDateTime);
    }

    private void assertDeleteInfo(DeleteOrganizationBoard deleteOrganizationBoard, Long adminMemberId) {
        assertThat(deleteOrganizationBoard.getDeletedMemberId()).isNull();
        assertThat(deleteOrganizationBoard.getDeletedAdminMemberId()).isEqualTo(adminMemberId);
    }

    private void assertDeleteOrganizationBoard(DeleteOrganizationBoard deleteOrganizationBoard, OrganizationBoard organizationBoard, Long adminMemberId) {
        assertThat(deleteOrganizationBoard.getSubDomain()).isEqualTo(organizationBoard.getSubDomain());
        assertThat(deleteOrganizationBoard.getMemberId()).isEqualTo(organizationBoard.getMemberId());
        assertThat(deleteOrganizationBoard.getCategory()).isEqualTo(organizationBoard.getCategory());
        assertThat(deleteOrganizationBoard.getTitle()).isEqualTo(organizationBoard.getTitle());
        assertThat(deleteOrganizationBoard.getContent()).isEqualTo(organizationBoard.getContent());
        assertThat(deleteOrganizationBoard.getLikesCount()).isEqualTo(organizationBoard.getLikesCount());
        assertThat(deleteOrganizationBoard.getBackUpId()).isEqualTo(organizationBoard.getId());
    }

}
