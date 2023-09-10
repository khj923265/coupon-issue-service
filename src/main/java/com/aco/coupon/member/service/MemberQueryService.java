package com.aco.coupon.member.service;

import com.aco.coupon.member.domain.Member;
import com.aco.coupon.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getList() {
        return memberRepository.findAll();
    }
}
