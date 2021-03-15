package com.potato.service.boardv2;

import com.potato.domain.boardV2.BoardV2;
import com.potato.domain.boardV2.BoardV2Repository;
import com.potato.domain.boardV2.organization.*;
import com.potato.exception.ConflictException;
import com.potato.exception.NotFoundException;
import com.potato.service.OrganizationMemberSetUpTest;
import com.potato.service.boardv2.dto.request.CreateOrganizationBoardRequest;
import com.potato.service.boardv2.dto.request.UpdateOrganizationBoardRequest;
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
    private BoardV2Repository boardRepository;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @Autowired
    private OrganizationBoardLikeRepository organizationBoardLikeRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardLikeRepository.deleteAllInBatch();
        organizationBoardRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
    }

    @Test
    void 그룹관리자가_게시물을_작성한다() {
        // given
        String title = "감자 신입 회원 모집";
        String content = "감자 동아리에서 신입 회원을 모집합니다";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 3, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 3, 5, 0, 0);
        OrganizationBoardType type = OrganizationBoardType.RECRUIT;

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
        assertOrganizationBoard(organizationBoardList.get(0), content, type, subDomain);

        List<BoardV2> board = boardRepository.findAll();
        assertThat(board).hasSize(1);
        assertBoard(board.get(0), title, memberId, startDateTime, endDateTime);
    }

    @Test
    void 그룹의_관리자가_게시글을_수정한다() {
        // given
        String title = "이후의 게시글";
        String content = "변경 이후의 내용";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 7, 0, 0);
        OrganizationBoardType type = OrganizationBoardType.EVENT;

        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, "이전의 게시글", OrganizationBoardType.RECRUIT);
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
        assertOrganizationBoard(organizationBoardList.get(0), content, type, subDomain);

        List<BoardV2> board = boardRepository.findAll();
        assertThat(board).hasSize(1);
        assertBoard(board.get(0), title, memberId, startDateTime, endDateTime);
    }

    @Test
    void 그룹의_관리자가_게시글을_수정하면_수정한_사람으로_게시글_작성자가_변경된다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 999L, "이전의 게시글", OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        UpdateOrganizationBoardRequest request = UpdateOrganizationBoardRequest.testBuilder()
            .organizationBoardId(organizationBoard.getId())
            .title("이후의 게시글")
            .content("변경 이후의 내용")
            .startDateTime(LocalDateTime.of(2021, 4, 1, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 4, 7, 0, 0))
            .type(OrganizationBoardType.EVENT)
            .build();

        // when
        organizationBoardService.updateBoard(subDomain, request, memberId);

        // then
        List<BoardV2> board = boardRepository.findAll();
        assertThat(board).hasSize(1);
        assertThat(board.get(0).getMemberId()).isEqualTo(memberId);
    }

    @Test
    void 그룹_게시물을_좋아요한다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 999L, "이전의 게시글", OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        // when
        organizationBoardService.likeOrganizationBoard(organizationBoard.getId(), memberId);

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
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 999L, "이전의 게시글", OrganizationBoardType.RECRUIT);
        organizationBoard.addLike(memberId);
        organizationBoardRepository.save(organizationBoard);

        // when & then
        assertThatThrownBy(() -> organizationBoardService.likeOrganizationBoard(organizationBoard.getId(), memberId)).isInstanceOf(ConflictException.class);
    }

    @Test
    void 그룹_게시물_좋아요를_취소한다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 999L, "이전의 게시글", OrganizationBoardType.RECRUIT);
        organizationBoard.addLike(memberId);
        organizationBoardRepository.save(organizationBoard);

        // when
        organizationBoardService.cancelOrganizationBoardLike(organizationBoard.getId(), memberId);

        // then
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAll();
        assertThat(organizationBoardList).hasSize(1);
        assertThat(organizationBoardList.get(0).getLikesCount()).isEqualTo(0);

        List<OrganizationBoardLike> organizationBoardLikeList = organizationBoardLikeRepository.findAll();
        assertThat(organizationBoardLikeList).hasSize(0);
    }

    @Test
    void 그룹_게시물_좋아요를_취소할때_좋아요를_누르지_않은경우_에러가_발생() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, 999L, "이전의 게시글", OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        // when & then
        assertThatThrownBy(() -> organizationBoardService.cancelOrganizationBoardLike(organizationBoard.getId(), memberId)).isInstanceOf(NotFoundException.class);
    }

    private void assertOrganizationBoardLike(OrganizationBoardLike organizationBoardLike, Long id, Long memberId) {
        assertThat(organizationBoardLike.getOrganizationBoard().getId()).isEqualTo(id);
        assertThat(organizationBoardLike.getMemberId()).isEqualTo(memberId);
    }


    private void assertBoard(BoardV2 board, String title, Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getMemberId()).isEqualTo(memberId);
        assertThat(board.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(board.getEndDateTime()).isEqualTo(endDateTime);
    }

    private void assertOrganizationBoard(OrganizationBoard organizationBoard, String content, OrganizationBoardType type, String subDomain) {
        assertThat(organizationBoard.getContent()).isEqualTo(content);
        assertThat(organizationBoard.getType()).isEqualTo(type);
        assertThat(organizationBoard.getSubDomain()).isEqualTo(subDomain);
    }

}
