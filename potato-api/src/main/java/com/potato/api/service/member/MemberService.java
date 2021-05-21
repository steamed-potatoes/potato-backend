package com.potato.api.service.member;

import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberMajor;
import com.potato.domain.domain.member.MemberRepository;
import com.potato.api.service.member.dto.request.SignUpMemberRequest;
import com.potato.api.service.member.dto.request.UpdateMemberRequest;
import com.potato.api.service.member.dto.response.MajorInfoResponse;
import com.potato.api.service.member.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long signUpMember(SignUpMemberRequest request) {
        MemberServiceUtils.validateNonExistsMember(memberRepository, request.getEmail());
        return memberRepository.save(request.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);
        return MemberInfoResponse.of(member);
    }

    @Transactional
    public MemberInfoResponse updateMemberInfo(UpdateMemberRequest request, Long memberId) {
        Member member = MemberServiceUtils.findMemberById(memberRepository, memberId);
        member.updateMemberInfo(request.getName(), request.getProfileUrl(), request.getMajor(), request.getClassNumber());
        return MemberInfoResponse.of(member);
    }

    public List<MajorInfoResponse> getMajors() {
        return Arrays.stream(MemberMajor.values())
            .map(MajorInfoResponse::of)
            .collect(Collectors.toList());
    }

}
