package com.potato.service.board;

import com.potato.domain.board.organization.*;
import com.potato.service.OrganizationMemberSetUpTest;
import com.potato.service.board.organization.OrganizationBoardRetrieveService;
import com.potato.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrganizationBoardRetrieveServiceTest extends OrganizationMemberSetUpTest {

    @Autowired
    private OrganizationBoardRetrieveService organizationBoardService;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAllInBatch();
    }

    @Test
    void 가장_최신_게시물_3개_불러온다() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardType.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3));

        //when
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrieveBoardsWithPagination(0, 3);

        //then
        assertThat(responses).hasSize(3);
        assertOrganizationBoardInfo(responses.get(0), organizationBoard3.getTitle(), organizationBoard3.getStartDateTime(),
            organizationBoard3.getEndDateTime(), organizationBoard3.getSubDomain(), organizationBoard3.getType());
        assertOrganizationBoardInfo(responses.get(1), organizationBoard2.getTitle(), organizationBoard2.getStartDateTime(),
            organizationBoard2.getEndDateTime(), organizationBoard2.getSubDomain(), organizationBoard2.getType());
        assertOrganizationBoardInfo(responses.get(2), organizationBoard1.getTitle(), organizationBoard1.getStartDateTime(),
            organizationBoard1.getEndDateTime(), organizationBoard1.getSubDomain(), organizationBoard1.getType());
    }

    @Test
    void 가장_최신_게시물_3개_불러올때_게시물이_없을_경우_빈리스트_반환() {
        //given
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrieveBoardsWithPagination(0, 3);

        //then
        assertThat(responses).isEmpty();
    }

    @DisplayName("게시물 4 이후부터 3개 조회했으니 [3, 2, 1]이 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_1() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard4 = OrganizationBoardCreator.create(subDomain, memberId, "title4", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard5 = OrganizationBoardCreator.create(subDomain, memberId, "title5", OrganizationBoardType.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3, organizationBoard4, organizationBoard5));

        //when
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrieveBoardsWithPagination(organizationBoard4.getId(), 3);

        //then
        assertThat(responses).hasSize(3);
        assertOrganizationBoardInfo(responses.get(0), organizationBoard3.getTitle(), organizationBoard3.getStartDateTime(),
            organizationBoard3.getEndDateTime(), organizationBoard3.getSubDomain(), organizationBoard3.getType());
        assertOrganizationBoardInfo(responses.get(1), organizationBoard2.getTitle(), organizationBoard2.getStartDateTime(),
            organizationBoard2.getEndDateTime(), organizationBoard2.getSubDomain(), organizationBoard2.getType());
        assertOrganizationBoardInfo(responses.get(2), organizationBoard1.getTitle(), organizationBoard1.getStartDateTime(),
            organizationBoard1.getEndDateTime(), organizationBoard1.getSubDomain(), organizationBoard1.getType());
    }

    @DisplayName("게시물 4 이후부터 2개 조회했으니 [3, 2]이 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_2() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard4 = OrganizationBoardCreator.create(subDomain, memberId, "title4", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard5 = OrganizationBoardCreator.create(subDomain, memberId, "title5", OrganizationBoardType.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3, organizationBoard4, organizationBoard5));

        //when
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrieveBoardsWithPagination(organizationBoard4.getId(), 2);

        //then
        assertThat(responses).hasSize(2);
        assertOrganizationBoardInfo(responses.get(0), organizationBoard3.getTitle(), organizationBoard3.getStartDateTime(),
            organizationBoard3.getEndDateTime(), organizationBoard3.getSubDomain(), organizationBoard3.getType());
        assertOrganizationBoardInfo(responses.get(1), organizationBoard2.getTitle(), organizationBoard2.getStartDateTime(),
            organizationBoard2.getEndDateTime(), organizationBoard2.getSubDomain(), organizationBoard2.getType());
    }

    @DisplayName("게시물 5 이후부터 3개 조회했으니 [4, 3, 2]이 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_3() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard4 = OrganizationBoardCreator.create(subDomain, memberId, "title4", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard5 = OrganizationBoardCreator.create(subDomain, memberId, "title5", OrganizationBoardType.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3, organizationBoard4, organizationBoard5));

        //when
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrieveBoardsWithPagination(organizationBoard5.getId(), 3);

        //then
        assertThat(responses).hasSize(3);
        assertOrganizationBoardInfo(responses.get(0), organizationBoard4.getTitle(), organizationBoard4.getStartDateTime(),
            organizationBoard4.getEndDateTime(), organizationBoard4.getSubDomain(), organizationBoard4.getType());
        assertOrganizationBoardInfo(responses.get(1), organizationBoard3.getTitle(), organizationBoard3.getStartDateTime(),
            organizationBoard3.getEndDateTime(), organizationBoard3.getSubDomain(), organizationBoard3.getType());
        assertOrganizationBoardInfo(responses.get(2), organizationBoard2.getTitle(), organizationBoard2.getStartDateTime(),
            organizationBoard2.getEndDateTime(), organizationBoard2.getSubDomain(), organizationBoard2.getType());
    }

    @Test
    void 게시물_스크롤_페이지네이션하는데_더이상_게시물이_존재하지_않을경우() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard1);

        //when
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrieveBoardsWithPagination(organizationBoard1.getId(), 3);

        //then
        assertThat(responses).isEmpty();
    }

    private void assertOrganizationBoardInfo(OrganizationBoardInfoResponse organizationBoardInfoResponse, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String subDomain, OrganizationBoardType type) {
        assertThat(organizationBoardInfoResponse.getTitle()).isEqualTo(title);
        assertThat(organizationBoardInfoResponse.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(organizationBoardInfoResponse.getEndDateTime()).isEqualTo(endDateTime);
        assertThat(organizationBoardInfoResponse.getType()).isEqualTo(type);
        assertThat(organizationBoardInfoResponse.getSubDomain()).isEqualTo(subDomain);
    }

}
