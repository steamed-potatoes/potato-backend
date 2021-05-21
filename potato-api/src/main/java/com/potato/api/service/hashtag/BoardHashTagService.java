package com.potato.api.service.hashtag;

import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.hashtag.BoardHashTag;
import com.potato.domain.domain.hashtag.BoardHashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardHashTagService {

    private final BoardHashTagRepository boardHashTagRepository;

    @Transactional
    public void addHashTag(BoardType type, Long boardId, Long memberId, List<String> hashTags) {
        List<BoardHashTag> boardHashTags = hashTags.stream()
            .distinct()
            .map(hashTag -> BoardHashTag.newInstance(type, boardId, memberId, hashTag))
            .collect(Collectors.toList());
        boardHashTagRepository.saveAll(boardHashTags);
    }

    @Transactional
    public void updateHashTags(BoardType type, Long boardId, Long memberId, List<String> hashTags) {
        List<BoardHashTag> hashTagList = BoardHashTagServiceUtils.findBoardHashTags(boardHashTagRepository, boardId, type);
        boardHashTagRepository.deleteAll(hashTagList);
        addHashTag(type, boardId, memberId, hashTags);
    }

}
