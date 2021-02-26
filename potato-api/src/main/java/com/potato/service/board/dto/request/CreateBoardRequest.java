package com.potato.service.board.dto.request;

import com.potato.domain.board.Board;
import com.potato.domain.board.Category;
import com.potato.domain.board.Visible;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CreateBoardRequest {

    @NotBlank
    private Visible visible;

    @NotBlank
    private String title;

    @NotBlank
    private String content;


    private String imageUrl;

    private Category category;

    @Builder(builderMethodName = "testBuilder")
    public CreateBoardRequest(@NotBlank Visible visible, @NotBlank String title, @NotBlank String content, String imageUrl, Category category) {
        this.visible = visible;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Board toEntity(Long organizationId, Long memberId) {
        return Board.instance(visible, title, content, imageUrl, category, memberId, organizationId);
    }
}
