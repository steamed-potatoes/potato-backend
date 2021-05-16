package com.potato.service.hashtag;

import com.potato.domain.board.BoardType;
import com.potato.domain.hashtag.BoardHashTag;
import com.potato.domain.hashtag.BoardHashTagRepository;
import com.potato.service.OrganizationMemberSetUpTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HashTagServiceTest extends OrganizationMemberSetUpTest {

    @Autowired
    private BoardHashTagService boardHashTagService;

    @Autowired
    private BoardHashTagRepository boardHashTagRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        boardHashTagRepository.deleteAll();
    }

    @Test
    void 특정_게시뭃에_해당하는_해시태그들을_저장한다() {
        // given
        Long boardId = 100L;
        List<String> hashTags = Arrays.asList("감자", "백엔드", "화이팅");

        // when
        boardHashTagService.addHashTag(BoardType.ORGANIZATION_BOARD, boardId, memberId, hashTags);

        // then
        List<BoardHashTag> hashTagList = boardHashTagRepository.findAll();
        assertThat(hashTagList).hasSize(3);
        assertHashTag(hashTagList.get(0), boardId, memberId, "감자");
        assertHashTag(hashTagList.get(1), boardId, memberId, "백엔드");
        assertHashTag(hashTagList.get(2), boardId, memberId, "화이팅");
    }

    @Test
    void 해시태그_저장시_중복된_해시태그가_있을경우_제거하고_저장한다() {
        // given
        String hashTag = "감자";
        Long boardId = 100L;
        List<String> hashTags = Arrays.asList(hashTag, hashTag, hashTag);

        // when
        boardHashTagService.addHashTag(BoardType.ORGANIZATION_BOARD, boardId, memberId, hashTags);

        // then
        List<BoardHashTag> hashTagList = boardHashTagRepository.findAll();
        assertThat(hashTagList).hasSize(1);
        assertHashTag(hashTagList.get(0), boardId, memberId, hashTag);
    }

    @Test
    void 해시태그를_수정하면_기존의_해시태그가_제거되고_새로운_해시태그가_저장된다() {
        // given
        String hashTags1 = "백엔드";
        String hashTags2 = "프론트";

        BoardType type = BoardType.ORGANIZATION_BOARD;
        Long boardId = 100L;
        boardHashTagRepository.save(BoardHashTag.newInstance(type, boardId, memberId, "감자"));

        // when
        boardHashTagService.updateHashTags(type, boardId, memberId, Arrays.asList(hashTags1, hashTags2));

        // then
        List<BoardHashTag> hashTagList = boardHashTagRepository.findAll();
        assertThat(hashTagList).hasSize(2);
        assertHashTag(hashTagList.get(0), boardId, memberId, hashTags1);
        assertHashTag(hashTagList.get(1), boardId, memberId, hashTags2);
    }

    private void assertHashTag(BoardHashTag boardHashTag, Long boardId, Long memberId, String hashtag) {
        assertThat(boardHashTag.getBoardId()).isEqualTo(boardId);
        assertThat(boardHashTag.getMemberId()).isEqualTo(memberId);
        assertThat(boardHashTag.getHashTag()).isEqualTo(hashtag);
    }

}
