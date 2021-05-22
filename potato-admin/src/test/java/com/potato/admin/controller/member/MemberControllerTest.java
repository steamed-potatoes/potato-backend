package com.potato.admin.controller.member;

import com.potato.admin.controller.ApiResponse;
import com.potato.admin.controller.ControllerTestUtils;
import com.potato.admin.service.member.dto.response.MemberInfoResponse;
import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberCreator;
import com.potato.domain.domain.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
public class MemberControllerTest extends ControllerTestUtils {

    private MemberMockMvc memberMockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        super.setup();
        memberMockMvc = new MemberMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        memberRepository.deleteAll();
    }

    @Test
    void 가입되어_있는_회원들_불러오는_경우() throws Exception {
        // given
        String token = administratorMockMvc.getMockAdminToken();
        Member member1 = MemberCreator.create("potato1@gmail.com");
        Member member2 = MemberCreator.create("potato2@gmail.com");
        memberRepository.saveAll(Arrays.asList(member1, member2));

        // when
        ApiResponse<List<MemberInfoResponse>> response = memberMockMvc.retrieveMember(token, 200);

        // then
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getEmail()).isEqualTo(member1.getEmail());
        assertThat(response.getData().get(1).getEmail()).isEqualTo(member2.getEmail());
    }

    @Test
    void 가입되어_있는_회원들_불러오는데_없는경우_빈배열을_가지고온다() throws Exception {
        // given
        String token = administratorMockMvc.getMockAdminToken();

        // when
        ApiResponse<List<MemberInfoResponse>> response = memberMockMvc.retrieveMember(token, 200);

        // then
        assertThat(response.getData()).isEmpty();
    }

}
