package com.potato.domain.domain.board.organization.repository.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardWithOrganizationDtoWithImage {

    private BoardWithOrganizationDto boardWithOrganizationDto;

    private List<String> imageUrlList;

    public static BoardWithOrganizationDtoWithImage of(BoardWithOrganizationDto boardWithOrganizationDto, List<String> imageUrlList) {
        return new BoardWithOrganizationDtoWithImage(boardWithOrganizationDto, imageUrlList);
    }

}
