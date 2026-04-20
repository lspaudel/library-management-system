package com.library.service.impl;

import com.library.dto.request.MemberRequest;
import com.library.dto.response.MemberResponse;
import com.library.entity.Member;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<MemberResponse> search(String q, int page, int size) {
        return memberRepository.searchMembers(q, PageRequest.of(page, size, Sort.by("name"))).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public MemberResponse getById(Long id) {
        return toResponse(findMember(id));
    }

    @Transactional(readOnly = true)
    public MemberResponse getByUserId(Long userId) {
        return toResponse(memberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found for user: " + userId)));
    }

    public MemberResponse update(Long id, MemberRequest request) {
        Member member = findMember(id);
        member.setName(request.getName());
        member.setPhone(request.getPhone());
        member.setAddress(request.getAddress());
        member.setMembershipExpiry(request.getMembershipExpiry());
        return toResponse(memberRepository.save(member));
    }

    public void delete(Long id) {
        memberRepository.delete(findMember(id));
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));
    }

    public MemberResponse toResponse(Member m) {
        return MemberResponse.builder()
                .id(m.getId()).userId(m.getUser().getId())
                .name(m.getName()).email(m.getUser().getEmail())
                .phone(m.getPhone()).address(m.getAddress())
                .membershipExpiry(m.getMembershipExpiry())
                .createdAt(m.getCreatedAt())
                .build();
    }
}
