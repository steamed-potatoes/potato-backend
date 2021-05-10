package com.potato.service.board;

import com.potato.domain.board.organization.*;
import com.potato.exception.model.ConflictException;
import com.potato.exception.model.NotFoundException;
import com.potato.service.OrganizationMemberSetUpTest;
import com.potato.service.board.organization.OrganizationBoardService;
import com.potato.service.board.organization.dto.request.CreateOrganizationBoardRequest;
import com.potato.service.board.organization.dto.request.LikeOrganizationBoardRequest;
import com.potato.service.board.organization.dto.request.UpdateOrganizationBoardRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrganizationBoardServiceTest extends OrganizationMemberSetUpTest {

    @Autowired
    private OrganizationBoardService organizationBoardService;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @Autowired
    private OrganizationBoardLikeRepository organizationBoardLikeRepository;

    @Autowired
    private DeleteOrganizationBoardRepository deleteOrganizationBoardRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardLikeRepository.deleteAllInBatch();
        organizationBoardRepository.deleteAllInBatch();
        deleteOrganizationBoardRepository.deleteAll();
    }

    @Test
    void 새로운_게시물을_작성하면_DB에_게시물_정보가_저장된다() {
        // given
        String title = "감자 신입 회원 모집";
        String content = "감자 동아리에서 신입 회원을 모집합니다";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 3, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 3, 5, 0, 0);
        OrganizationBoardCategory type = OrganizationBoardCategory.RECRUIT;

        CreateOrganizationBoardRequest request = CreateOrganizationBoardRequest.testBuilder()
            .title(title)
            .content(content)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .type(type)
            .build();

        // when
        organizationBoardService.createBoard(subDomain, request, memberId);

        // then
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAll();
        assertThat(organizationBoardList).hasSize(1);
        assertThat(organizationBoardList.get(0).getLikesCount()).isEqualTo(0);
        assertOrganizationBoard(organizationBoardList.get(0), title, content, type, startDateTime, endDateTime, subDomain, memberId);
    }

    @Test
    void 게시글을_수정하면_DB에_저장된_게시물_정보가_수정된다() {
        // given
        String title = "이후의 게시글";
        String content = "변경 이후의 내용";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 7, 0, 0);
        OrganizationBoardCategory type = OrganizationBoardCategory.EVENT;

        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, "이전의 게시글", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        UpdateOrganizationBoardRequest request = UpdateOrganizationBoardRequest.testBuilder()
            .organizationBoardId(organizationBoard.getId())
            .title(title)
            .content(content)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .type(type)
            .build();

        // when
        organizationBoardService.updateBoard(subDomain, request, memberId);

        // then
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAll();
        assertThat(organizationBoardList).hasSize(1);
        assertOrganizationBoard(organizationBoardList.get(0), title, content, type, startDateTime, endDateTime, subDomain, memberId);
    }

    @Test
    void 게시글을_수정하면_수정을_요청한_사람으로_게시글_작성자가_변경된다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 999L, "이전의 게시글", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        UpdateOrganizationBoardRequest request = UpdateOrganizationBoardRequest.testBuilder()
            .organizationBoardId(organizationBoard.getId())
            .title("이후의 게시글")
            .content("변경 이후의 내용")
            .startDateTime(LocalDateTime.of(2021, 4, 1, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 4, 7, 0, 0))
            .type(OrganizationBoardCategory.EVENT)
            .build();

        // when
        organizationBoardService.updateBoard(subDomain, request, memberId);

        // then
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAll();
        assertThat(organizationBoardList).hasSize(1);
        assertThat(organizationBoardList.get(0).getMemberId()).isEqualTo(memberId);
    }

    @Test
    void 그룹_게시물을_좋아요하면_게시물_좋아요_테이블에_새로운_컬럼이_추가된다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 999L, "이전의 게시글", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        LikeOrganizationBoardRequest request = LikeOrganizationBoardRequest.testInstance(organizationBoard.getId());

        // when
        organizationBoardService.likeOrganizationBoard(request, memberId);

        // then
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAll();
        assertThat(organizationBoardList).hasSize(1);
        assertThat(organizationBoardList.get(0).getLikesCount()).isEqualTo(1);

        List<OrganizationBoardLike> organizationBoardLikeList = organizationBoardLikeRepository.findAll();
        assertThat(organizationBoardLikeList).hasSize(1);
        assertOrganizationBoardLike(organizationBoardLikeList.get(0), organizationBoard.getId(), memberId);
    }

    @Test
    void 이미_해당_게시물에_좋아요를_누른경우_또_누를수_없다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 999L, "이전의 게시글", OrganizationBoardCategory.RECRUIT);
        organizationBoard.addLike(memberId);
        organizationBoardRepository.save(organizationBoard);

        LikeOrganizationBoardRequest request = LikeOrganizationBoardRequest.testInstance(organizationBoard.getId());

        // when & then
        assertThatThrownBy(() -> organizationBoardService.likeOrganizationBoard(request, memberId)).isInstanceOf(ConflictException.class);
    }

    @Test
    void 존재하지_않는_게시물에_좋아요를_누르면_에러가_발생한다() {
        // when & then
        assertThatThrownBy(() -> organizationBoardService.likeOrganizationBoard(LikeOrganizationBoardRequest.testInstance(999L), memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 그룹_게시물_좋아요를_취소요청하면_기존의_좋아요_컬럼이_사라진다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 999L, "이전의 게시글", OrganizationBoardCategory.RECRUIT);
        organizationBoard.addLike(memberId);
        organizationBoardRepository.save(organizationBoard);

        LikeOrganizationBoardRequest request = LikeOrganizationBoardRequest.testInstance(organizationBoard.getId());

        // when
        organizationBoardService.cancelLikeOrganizationBoard(request, memberId);

        // then
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAll();
        assertThat(organizationBoardList).hasSize(1);
        assertThat(organizationBoardList.get(0).getLikesCount()).isEqualTo(0);

        List<OrganizationBoardLike> organizationBoardLikeList = organizationBoardLikeRepository.findAll();
        assertThat(organizationBoardLikeList).hasSize(0);
    }

    @Test
    void 그룹_게시물_좋아요를_취소_요청시_좋아요를_누르지_않은경우_에러가_발생한다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 999L, "이전의 게시글", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        LikeOrganizationBoardRequest request = LikeOrganizationBoardRequest.testInstance(organizationBoard.getId());

        // when & then
        assertThatThrownBy(() -> organizationBoardService.cancelLikeOrganizationBoard(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 게시물을_삭제하면_백업이_되고_삭제된다() {
        //given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, "게시글", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        //when
        organizationBoardService.deleteOrganizationBoard(subDomain, organizationBoard.getId(), memberId);

        //then
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAll();
        assertThat(organizationBoardList).isEmpty();

        List<DeleteOrganizationBoard> deleteOrganizationBoardList = deleteOrganizationBoardRepository.findAll();
        assertThat(deleteOrganizationBoardList).hasSize(1);
        assertDeletedBoardOrganization(deleteOrganizationBoardList.get(0), organizationBoard.getId(), organizationBoard.getSubDomain(), organizationBoard.getCategory(), organizationBoard.getMemberId(),
            organizationBoard.getTitle(), organizationBoard.getStartDateTime(), organizationBoard.getEndDateTime());
    }

    @Test
    void 게시물을_삭제할때_해당_그룹의_게시물이_아닌경우_에러가_발생한다() {
        //given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, "title", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        //when & then
        assertThatThrownBy(
            () -> organizationBoardService.deleteOrganizationBoard("another-subDomain", organizationBoard.getId(), memberId)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 게시물이_없으면_삭제되지_않는다() {
        //when & then
        assertThatThrownBy(
            () -> organizationBoardService.deleteOrganizationBoard(subDomain, 999L, memberId)
        ).isInstanceOf(NotFoundException.class);
    }

    private void assertOrganizationBoardLike(OrganizationBoardLike organizationBoardLike, Long id, Long memberId) {
        assertThat(organizationBoardLike.getOrganizationBoard().getId()).isEqualTo(id);
        assertThat(organizationBoardLike.getMemberId()).isEqualTo(memberId);
    }

    private void assertOrganizationBoard(OrganizationBoard organizationBoard, String title, String content, OrganizationBoardCategory type, LocalDateTime startDateTime, LocalDateTime endDateTime, String subDomain, Long memberId) {
        assertThat(organizationBoard.getTitle()).isEqualTo(title);
        assertThat(organizationBoard.getContent()).isEqualTo(content);
        assertThat(organizationBoard.getCategory()).isEqualTo(type);
        assertThat(organizationBoard.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(organizationBoard.getEndDateTime()).isEqualTo(endDateTime);
        assertThat(organizationBoard.getSubDomain()).isEqualTo(subDomain);
        assertThat(organizationBoard.getMemberId()).isEqualTo(memberId);
    }

    private void assertDeletedBoardOrganization(DeleteOrganizationBoard deletedBoard, Long backUpId, String subDomain, OrganizationBoardCategory type, Long memberId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        assertThat(deletedBoard.getBackUpId()).isEqualTo(backUpId);
        assertThat(deletedBoard.getSubDomain()).isEqualTo(subDomain);
        assertThat(deletedBoard.getCategory()).isEqualTo(type);
        assertThat(deletedBoard.getDeletedMemberId()).isEqualTo(memberId);
        assertThat(deletedBoard.getMemberId()).isEqualTo(memberId);
        assertThat(deletedBoard.getTitle()).isEqualTo(title);
        assertThat(deletedBoard.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(deletedBoard.getEndDateTime()).isEqualTo(endDateTime);
    }

}
