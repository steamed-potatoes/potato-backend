package com.potato.admin.service.member;

import com.potato.admin.service.member.dto.response.MemberInfoResponse;
import com.potato.domain.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> retrieveMembersInfo() {
        return memberRepository.findAll().stream()
            .map(MemberInfoResponse::of)
            .collect(Collectors.toList());
    }

}
