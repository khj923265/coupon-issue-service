package com.aco.coupon.member.controller;

import com.aco.coupon.common.response.ApiResponseEntity;
import com.aco.coupon.member.domain.Member;
import com.aco.coupon.member.service.MemberQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("member")
public class MemberController {

    private final MemberQueryService memberQueryService;

    public MemberController(MemberQueryService memberQueryService) {
        this.memberQueryService = memberQueryService;
    }

    @GetMapping("/list")
    public ApiResponseEntity<List<Member>> getList() {
        List<Member> memberList = memberQueryService.getList();
        return ApiResponseEntity.ok(memberList);
    }
}
