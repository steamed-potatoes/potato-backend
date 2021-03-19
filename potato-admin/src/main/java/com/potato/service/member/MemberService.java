package com.potato.service.member;

import com.potato.domain.member.MemberRepository;
import com.potato.service.member.dto.response.MemberInfoResponse;
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
