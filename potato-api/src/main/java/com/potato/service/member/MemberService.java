package com.potato.service.member;

import com.potato.domain.member.MemberRepository;
import com.potato.service.member.dto.request.CreateMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long createMember(CreateMemberRequest request) {
        MemberServiceUtils.validateNonExistsMember(memberRepository, request.getEmail());
        return memberRepository.save(request.toEntity()).getId();
    }

}
